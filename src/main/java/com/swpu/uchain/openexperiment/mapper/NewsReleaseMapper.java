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
     * 根据是否发布来查询
     * @return
     */
    List<NewsRelease> selectAllByPublished();

    int updateByPrimaryKey(NewsRelease record);
}