package com.spaceobj.user.aop;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.utils.ServiceProceedingJoinPoint;
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
import sun.net.util.IPAddressUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author zhr_java@163.com
 * @date 2022/7/18 20:50
 */
@Component
@Aspect
@Order(1)
public class IpLimiting {

  @Autowired private RedisTemplate redisTemplate;

  private static final Logger LOG = LoggerFactory.getLogger(IpLimiting.class);

  /** 最大请求次数 */
  private static final int MAX_REQUEST_TIME = 3;

  /** 恶意请求次数 */
  private static final int MALICIOUS_REQUESTS = 5;

  @Pointcut("execution(public * com.spaceobj..controller.*.*(..))")
  public void doIpLimit() {}

  @Around("doIpLimit()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {

    try {
      String ip = getIPAddress();
      if (!isIpAddressCheck(ip)) {
        pjp = new ServiceProceedingJoinPoint(SaResult.error("请求参数错误"));
        return pjp.proceed();
      }

      // 如果是请求次数超过10次的，直接返回服务器异常
      if (redisTemplate.hasKey(ip)
          && (int) redisTemplate.boundValueOps(ip).get() >= MALICIOUS_REQUESTS) {
        pjp = new ServiceProceedingJoinPoint(SaResult.ok("服务器繁忙"));
        LOG.info("ip:{}", ip);
        return pjp.proceed();
      }

      // 可以将用户的ip和每秒请求的次数放入Redis中，如果当前用户每秒请求次数超过最大请求次数，返回请求频繁，请稍后重试！！；
      if (redisTemplate.hasKey(ip)) {

        redisTemplate.opsForValue().increment(ip);
        // 如果请求次数大于等于攻击次数，1天后解封
        if ((int) redisTemplate.boundValueOps(ip).get() >= MALICIOUS_REQUESTS) {
          LOG.info("ip:{}", ip);
          redisTemplate
              .opsForValue()
              .set(ip, (int) redisTemplate.boundValueOps(ip).get(), 1, TimeUnit.DAYS);
          pjp = new ServiceProceedingJoinPoint(SaResult.ok("服务器繁忙"));
          return pjp.proceed();
        }

      } else {
        redisTemplate.opsForValue().set(ip, 1, 1, TimeUnit.SECONDS);
      }

      if ((int) redisTemplate.boundValueOps(ip).get() > MAX_REQUEST_TIME) {
        pjp = new ServiceProceedingJoinPoint(SaResult.ok("请求频繁，请稍后重试"));
        return pjp.proceed();
      }
      return pjp.proceed();
    } catch (Exception e) {
      e.printStackTrace();
      pjp = new ServiceProceedingJoinPoint(SaResult.error("服务器异常"));
      return pjp.proceed();
    }
  }

  /**
   * IP正则校验
   *
   * @param address
   * @return
   */
  public static boolean isIpAddressCheck(String address) {

    boolean iPv4LiteralAddress = IPAddressUtil.isIPv4LiteralAddress(address);
    boolean iPv6LiteralAddress = IPAddressUtil.isIPv6LiteralAddress(address);
    // ip有可能是v4,也有可能是v6,滿足任何一种都是合法的ip
    if (!(iPv4LiteralAddress || iPv6LiteralAddress)) {
      return false;
    }
    return true;
  }

  /**
   * 获取用户IP
   *
   * @return
   */
  public static String getIPAddress() {

    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return requestAttributes.getRequest().getRemoteHost();
  }
}
