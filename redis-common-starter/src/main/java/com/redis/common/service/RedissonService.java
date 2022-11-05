package com.redis.common.service;

import com.core.utils.ExceptionUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author zhr_java@163.com
 * @date 2022/10/6 18:38
 */
public class RedissonService {

  @Autowired private RedissonClient redissonClient;

  /**
   * 获取锁-同一个线程可重入
   *
   * @param lockKey 锁的名称
   * @param waitTime 获取锁的等待时间
   * @param leaseTime 锁的持续时间
   * @param unit 时间的单位
   * @return 获取锁的结果
   */
  public Boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
    RLock lock = redissonClient.getLock(lockKey);
    try {
      // 尝试加锁，最多等待时间，上锁以后至少多久自动解锁
      boolean locked = lock.tryLock(waitTime, leaseTime, unit);
      if (locked) {
        return Boolean.TRUE;
      }
    } catch (InterruptedException e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return Boolean.FALSE;
    }
    return Boolean.FALSE;
  }

  public Boolean tryLock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    try {
      // 尝试加锁，最多等待时间，上锁以后至少多久自动解锁
      boolean locked = lock.tryLock(200, 20, TimeUnit.MICROSECONDS);
      if (locked) {
        return Boolean.TRUE;
      }
    } catch (InterruptedException e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return Boolean.FALSE;
    }
    return Boolean.FALSE;
  }

  /**
   * 解锁 - 重入的方式，所以同一个线程加了几次锁就要释放几次锁
   *
   * @param lockKey 锁的值
   */
  public boolean unLock(String lockKey) {
    try {
      RLock lock = redissonClient.getLock(lockKey);
      // 判断锁是否存在，和是否当前线程加的锁。
      if (null != lock && lock.isHeldByCurrentThread()) {
        lock.unlock();
        return Boolean.TRUE;
      }
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return Boolean.FALSE;
    }
    return Boolean.FALSE;
  }
}
