package com.bochao.file.service;

import com.bochao.common.entity.FileInfo;

import java.util.List;

/**
 * @Author 陈晓峰
 * @Description:
 * @Date:Created
 */
public interface FileInfoService {

    void saveFile(FileInfo fileInfo);
    List<FileInfo> getAllFileByIds(List<String> ids);
    FileInfo getFileById(String id);
    void delFileByIds(List<String> ids);
    void delFileById(String id);
}
