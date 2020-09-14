package com.bochao.file.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bochao.common.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author 陈晓峰
 * @Description:
 * @Date:Created
 */
public interface FileInfoMapper extends BaseMapper<FileInfo> {

//    List<FileInfo> getAllFileByIds(@Param("list") List<String> list);
//
//    FileInfo selectByPrimaryKey(String id);
//
//    int deleteByPrimaryKey(String id);
//
//    void saveFile(FileInfo fileInfo);
//
//    void delFileByIds( @Param("list") List<String> list);
}
