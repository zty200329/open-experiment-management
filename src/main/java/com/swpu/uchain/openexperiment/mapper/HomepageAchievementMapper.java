package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.HomepageAchievement;
import com.swpu.uchain.openexperiment.form.announcement.UpdateAchievementContentForm;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zty
 */
@Repository
public interface HomepageAchievementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomepageAchievement record);

    HomepageAchievement selectByPrimaryKey(Integer id);

    List<HomepageAchievement> selectAll();

    List<HomepageAchievement> selectAllPublished();

    int updateByPrimaryKey(UpdateAchievementContentForm updateNewsContentFor);

    int updateStatusByPrimaryKey(@Param("status") Integer status, @Param("id")Integer id);
}