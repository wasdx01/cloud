package com.bochao.common.mq.entity;

import com.bochao.common.entity.FileInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author 陈晓峰
 * @Description: 上传模型文件信息消息队列
 * @Date:Created in 2020/8/7
 */
@Getter
@Setter
public class ModelFileMqMessage extends MqMessage {
    private String fileInfoId;
    private String path;
    private String fileName;
    private String projectId;

    public ModelFileMqMessage(FileInfo fileInfo, String projectId) {
        this.fileInfoId = fileInfo.getId();
        this.path = fileInfo.getStorePath();
        this.projectId = projectId;
        this.fileName = fileInfo.getFileName();
    }

}
