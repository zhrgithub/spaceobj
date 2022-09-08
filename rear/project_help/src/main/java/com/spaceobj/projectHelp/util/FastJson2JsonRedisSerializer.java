package com.spaceobj.projectHelp.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author zhr_java@163.com
 * @date 2022/9/8 14:32
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

  private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

  private Class<T> clazz;

  public FastJson2JsonRedisSerializer(Class<T> clazz) {

    super();
    this.clazz = clazz;
  }

  @Override
  public byte[] serialize(T t) throws SerializationException {

    if (null == t) {
      return new byte[0];
    }
    return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
  }

  @Override
  public T deserialize(byte[] bytes) throws SerializationException {

    if (null == bytes || bytes.length <= 0) {
      return null;
    }
    String str = new String(bytes, DEFAULT_CHARSET);
    return JSON.parseObject(str, clazz, JSONReader.Feature.SupportAutoType);
  }
}
