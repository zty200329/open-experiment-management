package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import java.util.List;

public interface UserProjectGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserProjectGroup record);

    UserProjectGroup selectByPrimaryKey(Long id);

    List<UserProjectGroup> selectAll();

    int updateByPrimaryKey(UserProjectGroup record);
}