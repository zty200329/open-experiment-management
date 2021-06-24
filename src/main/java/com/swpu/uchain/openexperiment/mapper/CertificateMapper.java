package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Certificate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * zty
 */
@Repository
public interface CertificateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Certificate record);

    Certificate selectByPrimaryKey(Integer id);

    //获取本表中的
    List<Certificate> selectAll();

    int updateByPrimaryKey(Certificate record);

    Integer totalLine();

    /**
     * 满足某年份且为结题项目
     * @param year
     * @return
     */
    List<Certificate> selectByProjectStatus(@Param("year")String year);

    /**
     * 删除该表中的数据
     * @return
     */
    int deleteRequest();

    List<Certificate> selectByUserId(@Param("userId")String userId);

    int insertFinally(Certificate record);

    List<Certificate> slecetFinalByYear(@Param("year")String year);
}