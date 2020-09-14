package com.bochao.project.engineeringManagement.entity;

import com.bochao.common.pojo.CommonEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ProjectModel extends CommonEntity {
    /**
     * 主键
     */
    private String id;

    /**
     * 模型解析信息主键
     */
    private String modelId;

    /**
     * 模型文件ID
     */
    private String gimFileId;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 工程编号
     */
    private String projectNo;

    /**
     * 电压等级(10kV;35kV;110kV;220kV;330kV;500kV;800kV;1000kV)
     */
    private String voltageLevel;

    /**
     * 工程状态-GCZT(0:待建;1:在建;2:竣工;3:投运)
     */
    private Integer projectStatus;

    /**
     * 上传人
     */
    private String uploadPerson;

    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date uploadDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getGimFileId() {
        return gimFileId;
    }

    public void setGimFileId(String gimFileId) {
        this.gimFileId = gimFileId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getVoltageLevel() {
        return voltageLevel;
    }

    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getUploadPerson() {
        return uploadPerson;
    }

    public void setUploadPerson(String uploadPerson) {
        this.uploadPerson = uploadPerson;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
