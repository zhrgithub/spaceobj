package com.spaceobj.user.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.spaceobj.common.core.utils.ExceptionUtil;

import java.util.HashMap;

/**
 * 公钥秘钥生成器
 *
 * @author zhr_java@163.com
 * @date 2022/9/16 20:32
 */
public class GenerateRsa {

    /**
     * 初始化公钥私钥
     *
     * @return
     */
    public static HashMap<String, String> rsaGenerateKeyPair() {

        HashMap<String, String> rsa;
        try {
            rsa = SaSecureUtil.rsaGenerateKeyPair();
        } catch (Exception e) {
            ExceptionUtil.exceptionToString(e);
            e.printStackTrace();
            rsa = new HashMap<>();
            rsa.put("msg", "rsa初始化失败");
            return rsa;
        }
        return rsa;
    }

}
