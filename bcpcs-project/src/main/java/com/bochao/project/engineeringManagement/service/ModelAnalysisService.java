package com.bochao.project.engineeringManagement.service;

import com.bochao.common.util.FileUtil;
import com.bochao.project.engineeringManagement.entity.ModelAnalysis;
import com.bochao.project.engineeringManagement.mapper.ModelAnalysisMapper;
import com.bochao.project.model.service.FacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class ModelAnalysisService extends CommonServiceImpl<ModelAnalysis> {
    @Resource
    private FacadeService facadeService;
    @Resource
    private ModelAnalysisMapper modelAnalysisMapper;
    @Value("${fileDir:}")
    private String fileDir;

    @Override
    public CommonMapper<ModelAnalysis> getMapper() {
        return modelAnalysisMapper;
    }

    public void delModelByProjectId(String projectId) throws Exception {
        List<ModelAnalysis> list = modelAnalysisMapper.findByProjectId(projectId);
        list.forEach(t -> modelAnalysisMapper.deleteByPrimaryKey(t.getId()));
        //删除模型数据
        facadeService.delDataByProjectID(projectId);
        // 清除工序与模型绑定关系
//        constructUnitMapper.deleteByProjectId(projectId);
        String path = fileDir + "/bcedc/" + projectId;
        FileUtil.delDir(path);
    }

}
