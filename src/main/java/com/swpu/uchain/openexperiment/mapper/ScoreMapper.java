package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Score;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Score record);

    Score selectByPrimaryKey(Long id);

    List<Score> selectAll();

    int updateByPrimaryKey(Score record);
}