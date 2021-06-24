package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.config.UploadConfig;
import com.swpu.uchain.openexperiment.domain.Certificate;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.enums.MaterialType;
import com.swpu.uchain.openexperiment.mapper.CertificateMapper;
import com.swpu.uchain.openexperiment.mapper.ProjectFileMapper;
import com.swpu.uchain.openexperiment.util.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author zty
 * @Date 2020/2/29 下午11:46
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ExportExcelTest {

    @Autowired
    private CertificateMapper certificateMapper;
    @Autowired
    private ProjectFileMapper projectFileMapper;
    @Autowired
    private UploadConfig uploadConfig;


    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date());
    }
}