package com.bochao.project.engineeringManagement.entity;

import com.bochao.common.pojo.CommonEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ProjectSub extends CommonEntity {
    /**
     * 主键
     */
    private String id;

    /**
     * 父工程ID(t_project主键)
     */
    private String projectId;

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

    private String voltageName;

    public String getVoltageName() {
        return voltageName;
    }

    public void setVoltageName(String voltageName) {
        this.voltageName = voltageName;
    }

    /**
     * 工程状态-GCZT(0:待建;1:在建;2:竣工;3:投运)
     */
    private Integer projectStatus;

    /**
     * 开工日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startDate;

    /**
     * 竣工日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date completionDate;

    /**
     * 变电容量
     */
    private String substationCapacity;

    /**
     * 工程负责人
     */
    private String responsibler;

    /**
     * 所属地市
     */
    private String address;

    /**
     * 工程描述
     */
    private String summary;

    /**
     * 业主单位
     */
    private String ownerUnit;

    /**
     * 业主单位名称
     */
    private String ownerUnitName;

    /**
     * 建设单位
     */
    private String buildUnit;

    /**
     * 建设单位名称
     */
    private String buildUnitName;

    /**
     * 监理单位
     */
    private String supervisionUnit;

    /**
     * 监理单位名称
     */
    private String supervisionUnitName;

    /**
     * 设计单位
     */
    private String designUnit;

    /**
     * 设计单位名称
     */
    private String designUnitName;

    /**
     * 物资单位
     */
    private String materialUnit;

    /**
     * 物资单位名称
     */
    private String materialUnitName;

    /**
     * 施工单位
     */
    private String constructionUnit;

    /**
     * 施工单位名称
     */
    private String constructionUnitName;

    /**
     * 站点横坐标
     */
    private String siteX;

    /**
     * 站点纵坐标
     */
    private String siteY;

    /**
     * 变电类型-BDZLX(1:户内;2:户外;3:半户外)
     */
    private String transformerType;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 创建人id
     */
    private String createPersonId;

    /**
     * 网络计划日历c
     */
    private String calendars;
    /**
     * 同步工程信息
     */
    private String dataCenterProject;

    private String gimFileId;

    /**
     * 可研线路长度
     */
    private double lineLength;
    /**
     * 建设性质(数据字典：新建、扩建、改造)
     */
    private String constructionNature;
    /**
     * 建设类型(0:常规工程 1特高压工程 2默认常规工程)
     */
    private String constructionType;
    /**
     * 工程类型(0:变电 1:输电 2:输变电)
     */
    private String projectType;

    public String getGimFileId() {
        return gimFileId;
    }

    public void setGimFileId(String gimFileId) {
        this.gimFileId = gimFileId;
    }

    public String getDataCenterProject() {
        return dataCenterProject;
    }

    public void setDataCenterProject(String dataCenterProject) {
        this.dataCenterProject = dataCenterProject;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getResponsibler() {
        return this.responsibler;
    }

    public void setResponsibler(String responsibler) {
        this.responsibler = responsibler;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getOwnerUnit() {
        return this.ownerUnit;
    }

    public void setOwnerUnit(String ownerUnit) {
        this.ownerUnit = ownerUnit;
    }

    public String getBuildUnit() {
        return this.buildUnit;
    }

    public void setBuildUnit(String buildUnit) {
        this.buildUnit = buildUnit;
    }

    public String getSupervisionUnit() {
        return this.supervisionUnit;
    }

    public void setSupervisionUnit(String supervisionUnit) {
        this.supervisionUnit = supervisionUnit;
    }

    public String getDesignUnit() {
        return this.designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }

    public String getMaterialUnit() {
        return this.materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getConstructionUnit() {
        return this.constructionUnit;
    }

    public void setConstructionUnit(String constructionUnit) {
        this.constructionUnit = constructionUnit;
    }

    public String getSiteX() {
        return this.siteX;
    }

    public void setSiteX(String siteX) {
        this.siteX = siteX;
    }

    public String getSiteY() {
        return this.siteY;
    }

    public void setSiteY(String siteY) {
        this.siteY = siteY;
    }

    public String getTransformerType() {
        return this.transformerType;
    }

    public void setTransformerType(String transformerType) {
        this.transformerType = transformerType;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOwnerUnitName() {
        return ownerUnitName;
    }

    public void setOwnerUnitName(String ownerUnitName) {
        this.ownerUnitName = ownerUnitName;
    }

    public String getBuildUnitName() {
        return buildUnitName;
    }

    public void setBuildUnitName(String buildUnitName) {
        this.buildUnitName = buildUnitName;
    }

    public String getSupervisionUnitName() {
        return supervisionUnitName;
    }

    public void setSupervisionUnitName(String supervisionUnitName) {
        this.supervisionUnitName = supervisionUnitName;
    }

    public String getDesignUnitName() {
        return designUnitName;
    }

    public void setDesignUnitName(String designUnitName) {
        this.designUnitName = designUnitName;
    }

    public String getMaterialUnitName() {
        return materialUnitName;
    }

    public void setMaterialUnitName(String materialUnitName) {
        this.materialUnitName = materialUnitName;
    }

    public String getConstructionUnitName() {
        return constructionUnitName;
    }

    public void setConstructionUnitName(String constructionUnitName) {
        this.constructionUnitName = constructionUnitName;
    }
    public String getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(String createPersonId) {
        this.createPersonId = createPersonId;
    }

    public String getCalendars() {
        return calendars;
    }

    public void setCalendars(String calendars) {
        this.calendars = calendars;
    }


    public double getLineLength() {
        return lineLength;
    }

    public void setLineLength(double lineLength) {
        this.lineLength = lineLength;
    }

    public String getConstructionNature() {
        return constructionNature;
    }

    public void setConstructionNature(String constructionNature) {
        this.constructionNature = constructionNature;
    }

    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    public String getSubstationCapacity() {
        return substationCapacity;
    }

    public void setSubstationCapacity(String substationCapacity) {
        this.substationCapacity = substationCapacity;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}

