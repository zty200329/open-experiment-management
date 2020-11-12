package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.NewsRelease;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NewsReleaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NewsRelease record);

    NewsRelease selectByPrimaryKey(Integer id);

    List<NewsRelease> selectAll();

    int updateByPrimaryKey(NewsRelease record);
}