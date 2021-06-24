package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.NewsRelease;
import com.swpu.uchain.openexperiment.form.announcement.UpdateNewsContentForm;
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

    int updateByPrimaryKey(UpdateNewsContentForm updateNewsContentForm);

    /**
     * 根据主键更改状态
     * @param id
     * @return
     */
    int updateStatusByPrimaryKey1(@Param("id")Integer id);

    int updateStatusByPrimaryKey2(@Param("id")Integer id);
}