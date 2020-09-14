package com.bochao.project.engineeringManagement.entity;

import com.bochao.common.util.UUIDUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ModelAnalysis {
    /**
     * 主键
     */
    @ApiModelProperty("文件唯一标识ID")
    private String id;
    /**
     * 工程ID(t_project_sub主键)
     */
    @ApiModelProperty("工程ID")
    private String projectId;
    /**
     * GIM文件
     */
    @ApiModelProperty("GIM文件ID")
    private String gimFileId;

    /**
     * mongo:udfId
     */
    private String udfId;

    /**
     * 解析状态-MXJXZT(0:未开始;1:格式转换;2:转换失败;3:解析中;4:解析成功;5:解析失败)
     */
    @ApiModelProperty("解析状态-MXJXZT(0:未开始;1:格式转换;2:转换失败;3:解析中;4:解析成功;5:解析失败)")
    private Integer status;
    /**
     * 创建时间
     */
    private Date uploadDate;
    /**
     * 转换结束时间
     */
    private Date transferTime;
    /**
     * 模型解析结束时间
     */
    private Date modelTime;


    public ModelAnalysis() {
    }

    public ModelAnalysis(String projectId, String gimFileId) {
        this.projectId = projectId;
        this.gimFileId = gimFileId;
        this.id = UUIDUtil.getPrimaryKeyUUID();
        this.status = 0;
        this.uploadDate = new Date();
    }

}