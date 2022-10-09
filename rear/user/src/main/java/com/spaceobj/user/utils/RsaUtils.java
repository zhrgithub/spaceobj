package com.spaceobj.user.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.gson.Gson;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA公钥/私钥/签名工具包
 *
 * <p>字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 *
 * @author zhr_java@163.com
 * @date 2022/10/9 00:22
 */
public class RsaUtils {

  /** */
  /** 加密算法RSA */
  public static final String KEY_ALGORITHM = "RSA";

  /** */
  /** 签名算法 */
  public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

  /** */
  /** 获取公钥的key */
  private static final String PUBLIC_KEY = "RSAPublicKey";

  /** */
  /** 获取私钥的key */
  private static final String PRIVATE_KEY = "RSAPrivateKey";

  /** */
  /** RSA最大加密明文大小 */
  private static final int MAX_ENCRYPT_BLOCK = 117;

  /** */
  /** RSA最大解密密文大小 */
  private static final int MAX_DECRYPT_BLOCK = 128;

  /** */
  /**
   * 生成密钥对(公钥和私钥)
   *
   * @return
   * @throws Exception
   */
  public static Map<String, Object> genKeyPair() throws Exception {
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
    keyPairGen.initialize(1024);
    KeyPair keyPair = keyPairGen.generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    Map<String, Object> keyMap = new HashMap<String, Object>(2);
    keyMap.put(PUBLIC_KEY, publicKey);
    keyMap.put(PRIVATE_KEY, privateKey);
    return keyMap;
  }

  /** */
  /**
   * 用私钥对信息生成数字签名
   *
   * @param data 已加密数据
   * @param privateKey 私钥(BASE64编码)
   * @return
   * @throws Exception
   */
  public static String sign(byte[] data, String privateKey) throws Exception {
    byte[] keyBytes = Base64Util.decodeFromString(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initSign(privateK);
    signature.update(data);
    return Base64Util.encode(signature.sign());
  }

  /** */
  /**
   * 校验数字签名
   *
   * @param data 已加密数据
   * @param publicKey 公钥(BASE64编码)
   * @param sign 数字签名
   * @return
   * @throws Exception
   */
  public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
    byte[] keyBytes = Base64Util.decodeFromString(publicKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PublicKey publicK = keyFactory.generatePublic(keySpec);
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initVerify(publicK);
    signature.update(data);
    return signature.verify(Base64Util.decodeFromString(sign));
  }

  /** */
  /**
   * 私钥解密
   *
   * @param encryptedData 已加密数据
   * @param privateKey 私钥(BASE64编码)
   * @return
   * @throws Exception
   */
  public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
      throws Exception {
    byte[] keyBytes = Base64Util.decodeFromString(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.DECRYPT_MODE, privateK);
    int inputLen = encryptedData.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段解密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
        cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_DECRYPT_BLOCK;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    return decryptedData;
  }

  /** */
  /**
   * 公钥解密
   *
   * @param encryptedData 已加密数据
   * @param publicKey 公钥(BASE64编码)
   * @return
   * @throws Exception
   */
  public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
    byte[] keyBytes = Base64Util.decodeFromString(publicKey);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key publicK = keyFactory.generatePublic(x509KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.DECRYPT_MODE, publicK);
    int inputLen = encryptedData.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段解密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
        cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_DECRYPT_BLOCK;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    return decryptedData;
  }

  /**
   * 公钥加密，直接对类对象加密 先把对象转化成JSON类型，然后转化成字符数组，然后再进行加密，最后返回加密后的字节数组
   *
   * @param obj 被加密的目标对象
   * @param publicKey 公钥
   * @return 被加密对象为空，返回null，异常也返回null
   */
  public static byte[] encryptByPublicKey(Object obj, String publicKey) {
    if (ObjectUtils.isEmpty(obj)) {
      return null;
    }
    byte[] result = null;
    try {
      Gson gson = new Gson();
      byte[] objByte = gson.toJson(obj, obj.getClass()).getBytes("UTF-8");
      result = encryptByPublicKey(objByte, publicKey);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return result;
  }

  /**
   * 根据源对象、私钥解密后转化成目标对象,失败则返回null,源对象为空也返回null
   *
   * @param source 源对象
   * @param target 转化成目标对象
   * @param privateKey 解密的私钥
   * @param <T> 泛型对象
   * @return 异常则返回null，源对象为空也返回null
   */
  public static <T> T decryptByPrivateKey(Object source, Object target, String privateKey) {
    if (ObjectUtils.isEmpty(source)) {
      return null;
    }
    try {
      byte[] objByte = (byte[]) source;
      byte[] decodeBytes = decryptByPrivateKey(objByte, privateKey);
      String res = new String(decodeBytes, "UTF-8");
      return new Gson().fromJson(res, (Type) target.getClass());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /** */
  /**
   * 公钥加密
   *
   * @param data 源数据
   * @param publicKey 公钥(BASE64编码)
   * @return
   * @throws Exception
   */
  public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
    byte[] keyBytes = Base64Util.decodeFromString(publicKey);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key publicK = keyFactory.generatePublic(x509KeySpec);
    // 对数据加密
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, publicK);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段加密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_ENCRYPT_BLOCK;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return encryptedData;
  }

  /** */
  /**
   * 私钥加密
   *
   * @param data 源数据
   * @param privateKey 私钥(BASE64编码)
   * @return
   * @throws Exception
   */
  public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
    byte[] keyBytes = Base64Util.decodeFromString(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, privateK);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段加密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_ENCRYPT_BLOCK;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return encryptedData;
  }

  /** */
  /**
   * 获取私钥
   *
   * @param keyMap 密钥对
   * @return
   * @throws Exception
   */
  public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
    Key key = (Key) keyMap.get(PRIVATE_KEY);
    return Base64Util.encode(key.getEncoded());
  }

  /** */
  /**
   * 获取公钥
   *
   * @param keyMap 密钥对
   * @return
   * @throws Exception
   */
  public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
    Key key = (Key) keyMap.get(PUBLIC_KEY);
    return Base64Util.encode(key.getEncoded());
  }
}
