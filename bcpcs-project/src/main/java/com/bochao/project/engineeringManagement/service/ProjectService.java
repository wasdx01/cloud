package com.bochao.project.engineeringManagement.service;


import com.bochao.project.engineeringManagement.entity.Project;
import com.bochao.project.engineeringManagement.mapper.ProjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProjectService extends CommonServiceImpl<Project> {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public CommonMapper<Project> getMapper() {
        return projectMapper;
    }

    public Project getByProjectId(String id) {
        return getMapper().selectByPrimaryKey(id);
    }
}
