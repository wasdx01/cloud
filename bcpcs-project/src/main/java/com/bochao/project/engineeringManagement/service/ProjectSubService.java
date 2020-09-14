package com.bochao.project.engineeringManagement.service;

import com.bochao.project.engineeringManagement.entity.ProjectSub;
import com.bochao.project.engineeringManagement.mapper.ProjectSubMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProjectSubService extends CommonServiceImpl<ProjectSub> {

    @Resource
    private ProjectSubMapper projectSubMapper;


    @Override
    public CommonMapper<ProjectSub> getMapper() {
        return projectSubMapper;
    }

}
