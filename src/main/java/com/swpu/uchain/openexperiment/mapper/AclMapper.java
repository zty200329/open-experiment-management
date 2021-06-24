package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Acl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author panghu
 */
@Repository
public interface AclMapper {
    /**
     * 根据主键决定
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    int insert(Acl record);

    Acl selectByPrimaryKey(Long id);

    List<Acl> selectAll();

    int updateByPrimaryKey(Acl record);

    List<Acl> selectByUserId(@Param("userId") Long userId);

    List<Acl> selectByRandom(@Param("info") String info);

    Acl selectByUrl(String url);

    List<Acl> selectByRoleId(Long id);
}