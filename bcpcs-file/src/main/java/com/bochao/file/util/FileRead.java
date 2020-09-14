package com.bochao.file.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @Author 陈晓峰
 * @Description:
 */
public class FileRead {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public FileRead(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void read(File file) throws IOException {
        if (!file.exists()) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("faild");
        } else {
            String fileName = file.getName();
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && (userAgent.contains("MSIE") || userAgent.contains("Trident"))) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-Disposition", String.format("attachment; fileName=\"%s\"", fileName));
            response.setCharacterEncoding("UTF-8");

            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = fileInputStream.read(buffer, 0, 8192)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
        }
    }
    /**
     * 下载用数据库文件名
     * @param file
     * @param fileName
     * @throws IOException
     */
    public void read(File file,String fileName) throws IOException {
        if (!file.exists()) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("faild");
        } else {
            String userAgent = request.getHeader("User-Agent");
            String realFileNameReal=file.getName();
            String suffix = realFileNameReal.substring(realFileNameReal.lastIndexOf(".") + 1);
            fileName=fileName+"."+suffix;
            if (userAgent != null && (userAgent.contains("MSIE") || userAgent.contains("Trident"))) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-Disposition", String.format("attachment; fileName=\"%s\"", fileName));
            response.setHeader("Content-Length", Long.toString(file.length()));
            response.setCharacterEncoding("UTF-8");


            try(FileInputStream fileInputStream = new FileInputStream(file)) {
                //FileInputStream fileInputStream = new FileInputStream(file);
                OutputStream outputStream = response.getOutputStream();

                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = fileInputStream.read(buffer, 0, 8192)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
