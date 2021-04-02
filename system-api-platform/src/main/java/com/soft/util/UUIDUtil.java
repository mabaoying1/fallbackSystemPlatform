package com.soft.util;

import java.util.UUID;

/**
 * @ClassName UUIDUtil
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/19
 * @Version V1.0
 **/
public class UUIDUtil {

    /**
     * 获取无横杆(-)UUID </br>
     * @return
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();//转化为String对象
        //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
