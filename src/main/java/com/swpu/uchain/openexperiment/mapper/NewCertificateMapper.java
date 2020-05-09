package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.NewCertificate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NewCertificateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewCertificate record);

    NewCertificate selectByPrimaryKey(Long id);

    List<NewCertificate> selectAll();

    int updateByPrimaryKey(NewCertificate record);
}