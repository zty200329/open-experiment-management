package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.NewsRelease;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NewsReleaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NewsRelease record);

    NewsRelease selectByPrimaryKey(Integer id);

    List<NewsRelease> selectAll();

    /**
     * 查询所有发布的
     * @return
     */
    List<NewsRelease> selectAllByPublished();

    int updateByPrimaryKey(NewsRelease record);
}