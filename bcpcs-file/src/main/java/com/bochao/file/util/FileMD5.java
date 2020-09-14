package com.bochao.file.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * @Author
 * @Description:
 * @Date:Created in 15:22 2018/1/16
 * 文件摘要，md5校验码，相同文件不另在磁盘存储
 */
public class FileMD5 {

    protected static String byteArrayToHex(byte[] byteArr){
        char[] hexDigits={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] resultCharArr=new char[byteArr.length * 2];
        int index=0;
        for (byte b:byteArr){
            resultCharArr[index++] = hexDigits[b>>>4& 0xf];
            resultCharArr[index++] = hexDigits[b& 0xf];
        }
        return new String(resultCharArr);
    }
    public static String getFileMD5(InputStream fileInputStream) {
        int buffSize=1024*1024;
        DigestInputStream digestInputStream=null;
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            digestInputStream=new DigestInputStream(fileInputStream,messageDigest);
            byte[] buffer = new byte[buffSize];
            while (digestInputStream.read(buffer)>0);
            byte[] resultArr = messageDigest.digest();
            return byteArrayToHex(resultArr);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
                digestInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
