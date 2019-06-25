package com.tsien.mall.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/23 0023 18:17
 */

public class MD5Util {

    /**
     * 通过MD5加密字符串，编码为UTF-8
     *
     * @param origin 需要MD5加密的原始字符串
     * @return 经过MD加密的大写字符串
     */
    public static String md5EncodeUtf8(String origin) {
        origin += PropertiesUtil.getProperty("password.salt", "");
        return md5Encode(origin);
    }

    /**
     * 返回大写MD5
     *
     * @param origin 原始字符串
     * @return 将MD转换为大写
     */
    private static String md5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md5.digest(resultString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ignored) {
        }
        return resultString.toUpperCase();
    }

    /**
     * @param bytes 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte value : bytes) {
            stringBuilder.append(byteToHexString(value));
        }
        return stringBuilder.toString();
    }

    /**
     * 将字节转换为字符串
     *
     * @param b 字节
     * @return String
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }


    /**
     * 16进制的字典值
     */
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};
}
