package com.bochao.project.model.service;


import com.sun.jna.Library;
import com.sun.jna.Native;

public interface UgdFileParse extends Library{

    UgdFileParse INSTANCE = (UgdFileParse) Native.loadLibrary("BC_CDK_UDF_EX",UgdFileParse.class);

    int convert2udf(String var1, String var2, String var3, int var4);

}