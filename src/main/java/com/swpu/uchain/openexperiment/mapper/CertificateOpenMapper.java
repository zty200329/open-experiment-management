package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Certificate;
import com.swpu.uchain.openexperiment.domain.CertificateOpen;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * zty
 */
@Repository
public interface CertificateOpenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CertificateOpen record);

    CertificateOpen selectByPrimaryKey(Integer id);

    List<CertificateOpen> selectAll();

    int updateByPrimaryKey(CertificateOpen record);

}