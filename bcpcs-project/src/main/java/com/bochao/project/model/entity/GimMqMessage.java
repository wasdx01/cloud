package com.bochao.project.model.entity;

import com.bochao.common.mq.entity.MqMessage;
import com.bochao.project.engineeringManagement.entity.ModelAnalysis;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author 陈晓峰
 * @Description: 上传模型文件信息消息队列
 * @Date:Created in 2020/8/7
 */
@Getter
@Setter
public class GimMqMessage extends MqMessage {
    private ModelAnalysis modelAnalysis;
    private String path;
    private String name;
    private String projectType;

    public GimMqMessage(ModelAnalysis modelAnalysis, String path, String name, String projectType) {
        this.modelAnalysis = modelAnalysis;
        this.path = path;
        this.name = name;
        this.projectType = projectType;
    }
}
