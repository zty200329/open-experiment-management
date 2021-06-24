package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.FunctionGivesGrade;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FunctionGivesGradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FunctionGivesGrade record);

    FunctionGivesGrade selectByPrimaryKey(Long id);

    List<FunctionGivesGrade> selectAll();

    int updateByPrimaryKey(FunctionGivesGrade record);
}