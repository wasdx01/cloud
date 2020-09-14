package com.bochao.project.engineeringManagement.mapper;

import com.bochao.project.engineeringManagement.entity.ModelAnalysis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelAnalysisMapper extends CommonMapper<ModelAnalysis> {
    List<ModelAnalysis> findByProjectId(@Param("projectId") String projectId);
}