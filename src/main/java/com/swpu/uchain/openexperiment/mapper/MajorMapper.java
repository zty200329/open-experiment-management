package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Major;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface MajorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Major record);

    Major selectByPrimaryKey(Integer id);

    String selectById(String id);

    List<Major> selectAll();

    int updateByPrimaryKey(Major record);
}