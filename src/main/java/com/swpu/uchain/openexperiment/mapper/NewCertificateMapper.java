package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.NewCertificate;
import java.util.List;

public interface NewCertificateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewCertificate record);

    NewCertificate selectByPrimaryKey(Long id);

    List<NewCertificate> selectAll();

    int updateByPrimaryKey(NewCertificate record);
}