package com.bochao.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * ID生成工具
 */
public class UUIDUtil {

    /**
     * 生成主键UUID
     * @return
     */
    public static String getPrimaryKeyUUID() {
       return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 生成随机ID
     */
    public static String getTypeRandomId() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        return (sdf.format(new Date()).toString() + (new Random().nextInt(1000000)+1)).toString();
    }



}
