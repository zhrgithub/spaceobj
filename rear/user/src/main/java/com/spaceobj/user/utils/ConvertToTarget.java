package com.spaceobj.user.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author zhr_java@163.com
 * @date 2022/8/25 12:25
 */
public class ConvertToTarget<T> {

  /**
   * 获取kafka的消息，并且通过泛型转化成目的对象
   *
   * @param message
   * @param
   * @return
   */
  public static  <T> T getObject(Object message, Class<T> classOfT) {
    // 开启复杂处理Map方法
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    // fromJson
    Message messageResult =
        gson.fromJson(message.toString(), new TypeToken<Message>() {}.getType());
    return new Gson().fromJson(messageResult.getMsg(), classOfT);
  }
}
