package com.spaceobj.projectHelp.util;

import org.springframework.util.Base64Utils;

import java.io.*;

/**
 * @author zhr_java@163.com
 * @date 2022/9/18 11:30
 */
public class Base64Util {

  /** 文件读取缓冲区大小 */
  private static final int CACHE_SIZE = 1024;
  /**
   * base64解码，将编码后字符串类型的base64转化为解码后的字符串
   *
   * @param str
   * @return
   */
  public static String decode(String str) {
    String decodeStr = null;
    try {
      str = str.replaceAll(" +", "+");
      byte[] bytes = str.getBytes();
      decodeStr = bytesToHexString(Base64Utils.decode(bytes));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return decodeStr;
  }

  /**
   * base64解码，将编码后字符串类型的base64转化为解码后的字节数组
   *
   * @param str
   * @return
   */
  public static byte[] decodeFromString(String str) {

    return Base64Utils.decodeFromString(str);
  }

  /** */
  /**
   * 二进制数据编码为BASE64字符串
   *
   * @param bytes
   * @return
   * @throws Exception
   */
  public static String encode(byte[] bytes) throws Exception {
    return new String(Base64Util.bytesToHexString(bytes));
  }

  /**
   * 字节转字符串
   *
   * @param bytes
   * @return
   */
  public static String bytesToHexString(byte[] bytes) {

    StringBuffer sb = new StringBuffer(bytes.length);
    String temp = null;
    for (int i = 0; i < bytes.length; i++) {
      temp = Integer.toHexString(0xFF & bytes[i]);
      if (temp.length() < 2) {
        sb.append(0);
      }
      sb.append(temp);
    }
    return sb.toString();
  }

  /** */
  /**
   * 将文件编码为BASE64字符串
   *
   * <p>大文件慎用，可能会导致内存溢出
   *
   * @param filePath 文件绝对路径
   * @return
   * @throws Exception
   */
  public static String encodeFile(String filePath) throws Exception {
    byte[] bytes = fileToByte(filePath);
    return encode(bytes);
  }

  /** */
  /**
   * BASE64字符串转回文件
   *
   * @param filePath 文件绝对路径
   * @param base64 编码字符串
   * @throws Exception
   */
  public static void decodeToFile(String filePath, String base64) throws Exception {
    byte[] bytes = decodeFromString(base64);
    byteArrayToFile(bytes, filePath);
  }

  /** */
  /**
   * 文件转换为二进制数组
   *
   * @param filePath 文件路径
   * @return
   * @throws Exception
   */
  public static byte[] fileToByte(String filePath) throws Exception {
    byte[] data = new byte[0];
    File file = new File(filePath);
    if (file.exists()) {
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
      byte[] cache = new byte[CACHE_SIZE];
      int nRead = 0;
      while ((nRead = in.read(cache)) != -1) {
        out.write(cache, 0, nRead);
        out.flush();
      }
      out.close();
      in.close();
      data = out.toByteArray();
    }
    return data;
  }

  /** */
  /**
   * 二进制数据写文件
   *
   * @param bytes 二进制数据
   * @param filePath 文件生成目录
   */
  public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
    InputStream in = new ByteArrayInputStream(bytes);
    File destFile = new File(filePath);
    if (!destFile.getParentFile().exists()) {
      destFile.getParentFile().mkdirs();
    }
    destFile.createNewFile();
    OutputStream out = new FileOutputStream(destFile);
    byte[] cache = new byte[CACHE_SIZE];
    int nRead = 0;
    while ((nRead = in.read(cache)) != -1) {
      out.write(cache, 0, nRead);
      out.flush();
    }
    out.close();
    in.close();
  }
}
