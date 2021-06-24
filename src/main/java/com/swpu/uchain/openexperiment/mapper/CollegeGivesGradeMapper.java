package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.CollegeGivesGrade;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CollegeGivesGradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CollegeGivesGrade record);

    CollegeGivesGrade selectByPrimaryKey(Long id);

    List<CollegeGivesGrade> selectAll();

    int updateByPrimaryKey(CollegeGivesGrade record);
}