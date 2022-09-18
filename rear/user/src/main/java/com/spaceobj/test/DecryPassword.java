package com.spaceobj.test;

import cn.dev33.satoken.secure.SaSecureUtil;
import org.springframework.util.Base64Utils;

/**
 * @author zhr_java@163.com
 * @date 2022/9/18 10:43
 */
public class DecryPassword {

  private static String privateKey =
      "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALNnlItZoeYGYZDYLsews8nLCEc9MlZpJUN1d3b7ALk3PEGSSjxq1yE2EwHYkpBwycfLbmeaV4Ir2c0E0mYtMEcT6zj2SVBIvSPxis4K34AXpMo/h6PzwdMJVmyLvnkwzYNRCALknmBSmxIze4pGCpPLtHr3pGviX1VkrgAOq+mBAgMBAAECgYAw2ZJsA0L/NFuaqhLK0wWRe9RMOZCcUNMRtEmMPzpqNG0W2bOXEex0IZo5VXRLGjsHSRBN64vKaRBML/TuV32eVpG/oGz6QgX6w/b2kHumOEsGHIekOt9iruS0AsnqEHc2kYF4wnNmaTN6oxAYYCBd1P5Twz5Z1my5lvRF+irNFQJBAP55g2dadsCb6zqod4bxBMPa0qq8Mz/d8BsImHKE15Nc3RO9KvxdMnJmL857jrnKk9QZIbY6TPE08OzQohl35vcCQQC0et+TFajXnb7nx0KKJ5FZTv1PxDajVzflk4WFEDBDhFEhTZ9mgmH05Q1z5Qj1PGZRqs1yrR1H+mCDbep0Lj1HAkEA9iZSDgo6bksjuWS14aiHhG/JcW+EX1Xt6ChjVfrbj+a2zl4gE2aO4oun2KI5x8uFPRdmPZ+dR4B/P4L8GYiatwJASa38Bi0Kf/PrUu47EPG0WfRwX5coAykBLd063ibMvVOQV3s7/vptiS/VfSHctVlSIDcNpqpb0N27dwxxcbrK0wJAFDI2BO5WOgcuT58xgK/CoCdfZz9PM5CK0cy92sDV3+A8Qq2fu3Km8bXUxmL2Xlgdx4yGusmDwCJILizqsor/dg==";

  public static void main(String[] args) {
    //

    String password =
        "SiJny/tc4MOqFfChHghTWe65ZAqu2oh9rnfCEogcIIcBAOOjK8Tw00jP1rOlfZ+9SBfg8jANWsbGEZemFKqqviJy7gHN49aCNCl+sAhqOwXyADC54eVSQB+UaByTGfsnkzPcUYKrkBhqPjkdX2qKi1gmNibMem57F57cvAywihg=";
    byte[] bytes = password.getBytes();

    String decode = bytesToHexString(Base64Utils.decode(bytes));
    System.out.println(decode);

    String result = SaSecureUtil.rsaDecryptByPrivate(privateKey, decode);
    System.out.println(result);
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
