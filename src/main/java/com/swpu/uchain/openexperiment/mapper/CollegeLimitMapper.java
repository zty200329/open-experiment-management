package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.CollegeLimit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeLimitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CollegeLimit record);

    CollegeLimit selectByPrimaryKey(Integer id);

    List<CollegeLimit> selectAll();

    int updateByPrimaryKey(CollegeLimit record);

    /**
     * 根据项目类型和学院来查询
     * 普通项目1 重点项目2
     * @param college
     * @param projectType
     * @return
     */
    CollegeLimit selectByTypeAndCollege(@Param("college")Integer college, @Param("projectType") Integer projectType);

}