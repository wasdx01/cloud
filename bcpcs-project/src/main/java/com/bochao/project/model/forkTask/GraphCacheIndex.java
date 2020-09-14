package com.bochao.project.model.forkTask;

import com.bochao.bcedc.data.DBColumn;
import com.bochao.bcedc.data.DBTable;
import com.bochao.bcedc.data.PrimaryKey;

@DBTable(name = "graphCacheIndex")
public class GraphCacheIndex {
    @PrimaryKey
    private String id;
    @DBColumn(name = "projectId")
    private String projectId;
    @DBColumn(name = "udfId")
    private String udfId;
    @DBColumn(name = "graphCacheIndexName")
    private String graphCacheIndexName;
    @DBColumn(name = "graphCacheIndexPath")
    private String graphCacheIndexPath;
    @DBColumn(name = "graphType")
    private String graphType;
    @DBColumn(name = "lon")
    private Double lon;
    @DBColumn(name = "lat")
    private Double lat;
    @DBColumn(name = "height")
    private Double height;
    @DBColumn(name = "angle")
    private Double angle;
    @DBColumn(name = "xOffect")
    private Double xOffect;
    @DBColumn(name = "yOffect")
    private Double yOffect;
    @DBColumn(name = "zOffset")
    private Double zOffset;

    public GraphCacheIndex() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUdfId() {
        return udfId;
    }

    public void setUdfId(String udfId) {
        this.udfId = udfId;
    }

    public String getGraphCacheIndexName() {
        return graphCacheIndexName;
    }

    public void setGraphCacheIndexName(String graphCacheIndexName) {
        this.graphCacheIndexName = graphCacheIndexName;
    }

    public String getGraphCacheIndexPath() {
        return graphCacheIndexPath;
    }

    public void setGraphCacheIndexPath(String graphCacheIndexPath) {
        this.graphCacheIndexPath = graphCacheIndexPath;
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Double getxOffect() {
        return xOffect;
    }

    public void setxOffect(Double xOffect) {
        this.xOffect = xOffect;
    }

    public Double getyOffect() {
        return yOffect;
    }

    public void setyOffect(Double yOffect) {
        this.yOffect = yOffect;
    }

    public Double getzOffset() {
        return zOffset;
    }

    public void setzOffset(Double zOffset) {
        this.zOffset = zOffset;
    }
}


