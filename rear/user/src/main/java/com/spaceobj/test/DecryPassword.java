package com.spaceobj.test;

import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.utils.RsaUtils;

/**
 * @author zhr_java@163.com
 * @date 2022/9/18 10:43
 */
public class DecryPassword {

  private static String privateKey =
      "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALNnlItZoeYGYZDYLsews8nLCEc9MlZpJUN1d3b7ALk3PEGSSjxq1yE2EwHYkpBwycfLbmeaV4Ir2c0E0mYtMEcT6zj2SVBIvSPxis4K34AXpMo/h6PzwdMJVmyLvnkwzYNRCALknmBSmxIze4pGCpPLtHr3pGviX1VkrgAOq+mBAgMBAAECgYAw2ZJsA0L/NFuaqhLK0wWRe9RMOZCcUNMRtEmMPzpqNG0W2bOXEex0IZo5VXRLGjsHSRBN64vKaRBML/TuV32eVpG/oGz6QgX6w/b2kHumOEsGHIekOt9iruS0AsnqEHc2kYF4wnNmaTN6oxAYYCBd1P5Twz5Z1my5lvRF+irNFQJBAP55g2dadsCb6zqod4bxBMPa0qq8Mz/d8BsImHKE15Nc3RO9KvxdMnJmL857jrnKk9QZIbY6TPE08OzQohl35vcCQQC0et+TFajXnb7nx0KKJ5FZTv1PxDajVzflk4WFEDBDhFEhTZ9mgmH05Q1z5Qj1PGZRqs1yrR1H+mCDbep0Lj1HAkEA9iZSDgo6bksjuWS14aiHhG/JcW+EX1Xt6ChjVfrbj+a2zl4gE2aO4oun2KI5x8uFPRdmPZ+dR4B/P4L8GYiatwJASa38Bi0Kf/PrUu47EPG0WfRwX5coAykBLd063ibMvVOQV3s7/vptiS/VfSHctVlSIDcNpqpb0N27dwxxcbrK0wJAFDI2BO5WOgcuT58xgK/CoCdfZz9PM5CK0cy92sDV3+A8Qq2fu3Km8bXUxmL2Xlgdx4yGusmDwCJILizqsor/dg==";

  private static String publicKey =
      "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzZ5SLWaHmBmGQ2C7HsLPJywhHPTJWaSVDdXd2+wC5NzxBkko8atchNhMB2JKQcMnHy25nmleCK9nNBNJmLTBHE+s49klQSL0j8YrOCt+AF6TKP4ej88HTCVZsi755MM2DUQgC5J5gUpsSM3uKRgqTy7R696Rr4l9VZK4ADqvpgQIDAQAB";

  public static void main(String[] args) throws Exception {
    // base64解密后通过私钥进行解密
    // String password =
    //
    // "SiJny/tc4MOqFfChHghTWe65ZAqu2oh9rnfCEogcIIcBAOOjK8Tw00jP1rOlfZ+9SBfg8jANWsbGEZemFKqqviJy7gHN49aCNCl+sAhqOwXyADC54eVSQB+UaByTGfsnkzPcUYKrkBhqPjkdX2qKi1gmNibMem57F57cvAywihg=";
    // byte[] bytes = password.getBytes();
    //
    // String decode = bytesToHexString(Base64Utils.decode(bytes));
    // System.out.println(decode);
    //
    // String result = SaSecureUtil.rsaDecryptByPrivate(privateKey, decode);
    // System.out.println(result);

    //  测试自定义分块加密与解密算法
    SysUser sysUser = new SysUser();
    sysUser.setUsername("张三");
    sysUser.setEmail("zhr_java@163.com");
    sysUser.setPassword("2121212");
    sysUser.setVersion(666L);

    // System.out.println(sysUser.toString());
    String testData = "cajsnakjcnsknsdjdcjac";

    byte[] encodeBytes =
        RsaUtils.encryptByPublicKey(sysUser.toString().getBytes("UTF-8"), publicKey);
    byte[] decodeBytes = RsaUtils.decryptByPrivateKey(encodeBytes, privateKey);

    byte[] srtbyte = null;

    srtbyte = testData.getBytes("UTF-8");

    String res = new String(decodeBytes, "UTF-8");

    System.out.println("解码后的数据" + res);
  }

  public static byte[] toByteArray(String hexString) {
    hexString = hexString.toLowerCase();
    final byte[] byteArray = new byte[hexString.length() / 2];
    int k = 0;
    for (int i = 0; i < byteArray.length; i++) { // 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
      byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
      byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
      byteArray[i] = (byte) (high << 4 | low);
      k += 2;
    }
    return byteArray;
  }

  public static String toHexString(byte[] byteArray) {
    String str = null;
    if (byteArray != null && byteArray.length > 0) {
      StringBuffer stringBuffer = new StringBuffer(byteArray.length);
      for (byte byteChar : byteArray) {
        stringBuffer.append(String.format("%02X", byteChar));
      }
      str = stringBuffer.toString();
    }
    return str;
  }

  private static byte[] hexStringToBytes(String hex) {

    int len = (hex.length() / 2);
    hex = hex.toUpperCase();
    byte[] result = new byte[len];
    char[] chars = hex.toCharArray();
    for (int i = 0; i < len; i++) {
      int pos = i * 2;
      result[i] = (byte) (toByte(chars[pos]) << 4 | toByte(chars[pos + 1]));
    }
    return result;
  }

  private static byte toByte(char c) {

    return (byte) "0123456789ABCDEF".indexOf(c);
  }

  private static String bytesToHexString(byte[] bytes) {

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
}
