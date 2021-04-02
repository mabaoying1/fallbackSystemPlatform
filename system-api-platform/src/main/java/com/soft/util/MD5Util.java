package com.soft.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ClassName MD5Util
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/19
 * @Version V1.0
 **/
public class MD5Util {

    /**
     * 传入源数据和加密key,拼接【data+key】生成UPPER后的加密 <br>
     * @param data 传入数据 <br>
     * @param key  传入key <br>
     * @return
     */
    public static String dzMd5(String data,String key){
        String md5str = DigestUtils.md5Hex(data + key);
        return md5str.toUpperCase();
    }

    /**
     * 传入要MD5数据 <br>
     * @param data
     * @return
     */
    public static String dzMd5(String data){
        String md5str = DigestUtils.md5Hex(data);
        return md5str.toUpperCase();
    }

    /**
     * MD5验证方法
     * @param text
     * @param key
     * @param md5
     * @return
     * @throws Exception
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text, String key, String md5) throws Exception {
        String md5str = dzMd5(text, key);
        if (md5str.equalsIgnoreCase(md5)) {
            return true;
        }
        return false;
    }

}
