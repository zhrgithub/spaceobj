package com.spaceobj.project.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * dto、bo、vo、pojo转换
 *
 * @author zhr_java@163.com
 * @date 2022/9/3 23:21
 */
public class BeanConvertToTargetUtils {
  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    for (PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null||srcValue=="") {
        emptyNames.add(pd.getName());
      }
    }

    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  public static void copyNotNullProperties(Object source, Object target) {
    String[] ignoreProperties = getNullPropertyNames(source);
    BeanUtils.copyProperties(source, target, ignoreProperties);
  }
}
