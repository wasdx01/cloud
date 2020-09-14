package com.bochao.project.engineeringManagement.entity;

import java.util.Date;

public class Project {
    /**
     * 主键
     */
    private String id;

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
     * 开工日期
     */
    private Date startDate;

    /**
     * 竣工日期
     */
    private Date completionDate;

    /**
     * 所属地点
     */
    private String address;

    /**
     * 工程概述
     */
    private String describe;

    /**
     * 创建时间
     */
    private Date createTime;


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNo() {
        return this.projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getVoltageLevel() {
        return this.voltageLevel;
    }

    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    public Integer getProjectStatus() {
        return this.projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCompletionDate() {
        return this.completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}

