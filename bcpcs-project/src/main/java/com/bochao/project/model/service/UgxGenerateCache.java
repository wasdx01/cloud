package com.bochao.project.model.service;


import com.sun.jna.Library;
import com.sun.jna.Native;

public interface UgxGenerateCache extends Library{

    UgxGenerateCache INSTANCE = (UgxGenerateCache) Native.loadLibrary("BC_TIF_ANALYSIS_EX",UgxGenerateCache.class);

    /**
     * ugx构建缓存
     * @param i_UgxFilePathList ugx文件路径集合
     * @param i_OutputPath 生成缓存图层到指定目录
     * @param i_layerName 图层名
     * @param floats 设置LOG距离列表
     * @param floatThrows 丢弃列表
     * @param length 长
     * @param width 宽
     * @param nMaxLodLevel 设置最大LOG级别
     * @param nMaxTreeDepth 设置八叉树最大深度
     * @param nRootVolumeFactor 设置根节点体积因子
     * @param timeOut 设置超时时间（毫秒）
     * @return
     */
    int  UgxGenerateEx(String[] i_UgxFilePathList,
                       String i_OutputPath,
                       String i_layerName,
                       float[] floats,
                       float[] floatThrows,
                       float length,
                       float width,
                       int nMaxLodLevel,
                       int nMaxTreeDepth,
                       float nRootVolumeFactor,
                       int timeOut);

}