package com.bochao.file.util;

import com.bochao.common.entity.FileInfo;
import com.bochao.common.util.Md5Utils;
import com.bochao.common.util.UUIDUtil;
import com.bochao.file.service.FileInfoService;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

/**
 * @Author
 * @Description:
 * @Date:Created in 13:28 2018/3/20
 * 方便其它接口调用
 * fileInfoService 操作数据库文件表的服务类
 */
public class FileWrite {
    private FileInfoService fileInfoService;

    public FileWrite(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

    /**
     * 通过md5校验文件内容是否一致，如果文件已存在，则不再写入硬盘，并返回一个 FileInfo 对象
     * 追加条件 文件名也是校验条件
     * @param file
     * @param fileRootPath
     * @return
     * @throws IOException
     */
    public FileInfo write(MultipartFile file, String fileRootPath) {
        String md5=null;
        FileInfo fileInfo=null;
        try {
            md5=FileMD5.getFileMD5(file.getInputStream());
            return write(file,fileRootPath,md5);
        } catch (IOException e) {
            return getFileInfo(file);
        }
    }


    /**
     * 将文件写入磁盘，并返回一个 FileInfo 对象
     *
     * @param file
     * @param fileRootPath
     * @return
     * @throws IOException
     */
    protected  FileInfo write(MultipartFile file, String fileRootPath, String md5){
        InputStream is = null;
        FileInfo fileInfo=null;
        try {
            long length = 0;
            String storeName = Md5Utils.getMD5Uppercase(file.getOriginalFilename());
            File tempFile = new File(fileRootPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            is = file.getInputStream();
            length = writeFileByStream(fileRootPath +"/"+ storeName+"."+getFileSuffix(file.getOriginalFilename()),is);
            fileInfo = getFileInfo(file, fileRootPath, storeName, length,md5);

            fileInfoService.saveFile(fileInfo);
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return getFileInfo(file);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public  FileInfo write(MultipartFile file, String fileRootPath, String name, String type){
        InputStream is = null;
        try {
            String md5=FileMD5.getFileMD5(file.getInputStream());
            long length = 0;
            String storeName = Md5Utils.getMD5Uppercase(file.getOriginalFilename());
            File tempFile = new File(fileRootPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            is = file.getInputStream();
            length = writeFileByStream(fileRootPath +"/"+ storeName+name,is);
            String suffix=getFileSuffix(name);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(name.substring(0,name.lastIndexOf(".")));
            fileInfo.setStorePath(fileRootPath +"/"+ storeName + name);
            fileInfo.setFileStoreName(storeName);
            fileInfo.setFileSuffix(suffix);
            fileInfo.setFileSize(length);
            fileInfo.setMd5(md5);
            fileInfo.setCreateTime(new Date());
            fileInfoService.saveFile(fileInfo);
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return getFileInfo(file);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public long writeFileByStream(String path, InputStream is) {
        FileOutputStream fos = null;
        long length = 0;
        try {
            fos = new FileOutputStream(path);
            int len;
            byte[] buffer = new byte[1024 * 1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                length += len;
                fos.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return length;
    }

    /**
     * 拼装 FileInfo 对象
     *
     * @param file
     * @param fileRootPath
     * @param storeName
     * @param length
     * @return
     */
    protected FileInfo getFileInfo(MultipartFile file, String fileRootPath, String storeName, Long length, String md5) {
        String suffix=getFileSuffix(file.getOriginalFilename());
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(UUIDUtil.getPrimaryKeyUUID());
        fileInfo.setFileName(getFileRealName(file.getOriginalFilename()));
        fileInfo.setStorePath(fileRootPath +"/"+ storeName +"."+ suffix);
        fileInfo.setFileStoreName(storeName);
        fileInfo.setFileSuffix(suffix);
        fileInfo.setFileSize(length);
        fileInfo.setMd5(md5);
        fileInfo.setCreateTime(new Date());
        return fileInfo;
    }

    /**
     * 系统存储失败时，拼装 FileInfo 对象
     *
     * @param file
     * @return
     */
    protected FileInfo getFileInfo(MultipartFile file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(getFileRealName(file.getOriginalFilename()));
        fileInfo.setFileSuffix(getFileSuffix(file.getOriginalFilename()));
        return fileInfo;
    }

    /**
     * 获得文件源名称
     * @param name
     * @return
     */
    protected String getFileRealName(String name) {
        return name.replaceAll("(.*\\\\)?(.*)\\.\\w+$", "$2");
    }

    /**
     * 获得文件后缀
     * @param name
     * @return
     */
    protected String getFileSuffix(String name){
        return name.replaceAll(".*\\.(\\w+$)", "$1");
    }

    /**
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public FileInfo copyFileUsingFileStreams(File source, File dest,FileInfo fileInfo)
            throws Exception {

        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            FileInfo fileInfoNew = (FileInfo) fileInfo.clone();
            fileInfoNew.setStorePath(dest.getPath());
            fileInfoNew.setFileName(getFileRealName(dest.getName()));
            //fileInfoNew.setMd5();
            fileInfoService.saveFile(fileInfoNew);
            return fileInfoNew;
        } finally {
            input.close();
            output.close();
        }
    }
}
