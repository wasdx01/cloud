package com.bochao.project.model.service;


import java.io.InputStream;


/**
 * @Author 陈晓峰
 * @Description //TODO
 * @Date  2020/8/19
 **/
public interface FacadeService {


    /**
     * 上传UDF
     * @author 陈晓峰
     * @date 2019-07-02 16:59
     * @param projectId
     * @param udfName
     * @param stream
     * @return java.lang.String
     */
    String addUdf(String projectId, String udfName, InputStream stream, String projectType);

    void delDataByProjectID(String projectId) throws Exception;
}
