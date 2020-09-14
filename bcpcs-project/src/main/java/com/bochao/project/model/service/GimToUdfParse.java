package com.bochao.project.model.service;

import java.io.InputStream;

/**
 * @Author 宋义乐
 * @Description:
 * @Date:Created in 11:23 2018/11/7
 */
public interface GimToUdfParse {

    /**
     * gim转udf接口类
     * @param stream gim文件流
     * @param name gim文件名名称
     * @param type 输电或者变电
     * @return String
     */
    String convert2udf(InputStream stream, String name, String type) throws Exception;

}
