package com.bochao.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 文件操作工具类
 */
public class FileUtil {

    /*
     *@desc 删除目录以及目录下的所有文件,如果包含子目录以及子目录下所有文件
     **/
    public static boolean delDir(String path){
        if(path==null || "".equals(path))
            return true;
        File dir=new File(path);
        boolean status = true;
        if(dir.exists()){
            File[] tmp=dir.listFiles();
            if(tmp!=null)
            for(int i=0;i<tmp.length;i++){
                if(tmp[i].isDirectory()){
                    status = delDir(path+"/"+tmp[i].getName());
                }else{
                    tmp[i].delete();
                }
            }
            boolean b = dir.delete();
            status = ( status && b );
        }
        return status;
    }

    /**
     * 读取文件为字符串
     */
    public static String readJsonFile(InputStream is) {
        StringBuilder jsonStr = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                jsonStr.append(tempString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr.toString();
    }

    /**
     * 强制删除文件夹
     * @author 吕洋洋
     * @date 2019-10-14 14:53
     * @param path 文件或文件夹
     */
    public static void forceDelete(File path) {
        if (null != path) {
            if (!path.exists()) {
                return;
            }
            if (path.isFile()) {
                boolean result = path.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // 回收资源
                    result = path.delete();
                }
            }
            File[] files = path.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    forceDelete(files[i]);
                }
            }
            path.delete();
        }
    }
}
