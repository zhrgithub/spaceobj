package com.spaceobj.advertise.aop;

import com.spaceobj.advertise.constent.ServiceProceedingJoinPoint;
import com.spaceobj.advertise.service.impl.JdAdvertiseServiceImpl;
import com.spaceobj.advertise.utils.ResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * @author zhr_java@163.com
 * @date 2022/7/18 20:50
 */
@Component
@Aspect
@Order(1)
public class IpLimiting {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(JdAdvertiseServiceImpl.class);

    /**
     * 最大请求次数
     */
    private static final int MAX_REQUEST_TIME = 5;

    /**
     * 恶意请求次数
     */
    private static final int MALICIOUS_REQUESTS = 10;

    @Pointcut("execution(public * com.spaceobj..controller.*.*(..))")
    public void doIpLimit() {

    }

    @Around("doIpLimit()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        try {

            //获取用户IP
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String ip = requestAttributes.getRequest().getRemoteHost();

            //判断用户的ip是否是属于国内，如果不是国内ip就永久锁定，
            //TODO

            // 如果是请求次数超过10次的，直接返回服务器异常
            if (redisTemplate.hasKey(ip) && (int) redisTemplate.boundValueOps(ip).get() >= MALICIOUS_REQUESTS) {
                pjp = new ServiceProceedingJoinPoint(ResultData.busy());
                LOG.info("ip:{}", ip);
                return pjp.proceed();
            }

            //可以将用户的ip和每秒请求的次数放入Redis中，如果当前用户每秒请求次数超过最大请求次数，返回请求频繁，请稍后重试！！；
            if (redisTemplate.hasKey(ip)) {

                redisTemplate.opsForValue().increment(ip);
                //如果请求次数大于等于攻击次数，1天后解封
                if ((int) redisTemplate.boundValueOps(ip).get() >= MALICIOUS_REQUESTS) {
                    LOG.info("ip:{}", ip);
                    redisTemplate.opsForValue().set(ip, (int) redisTemplate.boundValueOps(ip).get(), 1, TimeUnit.DAYS);
                    pjp = new ServiceProceedingJoinPoint(ResultData.busy());
                    return pjp.proceed();
                }

            } else {
                redisTemplate.opsForValue().set(ip, 1, 1, TimeUnit.SECONDS);
            }

            if ((int) redisTemplate.boundValueOps(ip).get() > MAX_REQUEST_TIME) {
                pjp = new ServiceProceedingJoinPoint(ResultData.frequently());
                return pjp.proceed();
            }
            return pjp.proceed();
        } catch (Exception e) {
            LOG.error("广告服务ip切面拦截异常",e.getMessage());
            e.printStackTrace();
            pjp = new ServiceProceedingJoinPoint(ResultData.error("服务器异常"));
            return pjp.proceed();
        }

    }

}
