package com.bochao.file.service;

import com.bochao.common.entity.FileInfo;
import com.bochao.common.util.FileUtil;
import com.bochao.file.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 陈晓峰
 * @Description:
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {
    @Resource
    private FileInfoMapper fileInfoMapper;

    @Override
    public void saveFile(FileInfo fileInfo) {
        fileInfoMapper.insert(fileInfo);
    }

    @Override
    public List<FileInfo> getAllFileByIds(List<String> ids) {
        return fileInfoMapper.selectBatchIds(ids);
    }

    @Override
    public FileInfo getFileById(String fid) {
        return fileInfoMapper.selectById(fid);
    }

    @Override
    public void delFileByIds(List<String> ids) {
        ids.forEach(t->deletePhysicalFile(t));
        fileInfoMapper.deleteBatchIds(ids);
    }
    @Override
    public void delFileById(String id) {
        deletePhysicalFile(id);
        fileInfoMapper.deleteById(id);
    }

    private void deletePhysicalFile(String id){
        FileInfo f = getFileById(id);
        if(f!=null){
            FileUtil.delDir(f.getStorePath());
        }
    }
}
