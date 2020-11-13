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
     * @param status
     * @param id
     * @return
     */
    int updateStatusByPrimaryKey(@Param("status")Short status,@Param("id")Integer id);
}