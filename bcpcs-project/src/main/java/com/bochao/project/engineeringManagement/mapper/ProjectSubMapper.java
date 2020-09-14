package com.bochao.project.engineeringManagement.mapper;

import com.bochao.project.engineeringManagement.entity.ProjectSub;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectSubMapper extends CommonMapper<ProjectSub> {
    List<ProjectSub> findByProjectName(@Param("projectName") String projectName);

    List<ProjectSub> findByProjectNo(@Param("projectNo") String projectNo);

    ProjectSub findForEntity(@Param("id") String id);


    List<ProjectSub> findForList(@Param("projectName") String projectName, @Param("projectNo") String projectNo);

    /**
     * 根据权限查询
     */
    List<ProjectSub> findForListByRole(@Param("projectName") String projectName, @Param("projectNo") String projectNo, @Param("projectIdList") List<String> projectIdList);
    List<ProjectSub> findAllForList();
    /**
     * 根据用户id和用户所属单位id查询
     */
    List<ProjectSub> findForListByUserOROrg(@Param("projectName") String projectName, @Param("projectNo") String projectNo, @Param("orgId") Integer orgId, @Param("userId") Integer userId);


}