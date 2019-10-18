package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author panghu
 */
@Api
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ProjectGroupMapper projectGroupMapper;

    @GetMapping("/getProjectGroupDetailVOByProjectId")
    public Object getProjectGroupDetailVOByProjectId(){
        return Result.success(projectGroupMapper.getProjectGroupDetailVOByProjectId(1L));
    }


    @Test
    public  void readAndWriterTest4() {
        File file = new File("C:\\Users\\dengg\\Desktop\\开放性实验\\说明.docx");
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            String doc1 = extractor.getText();
            System.out.println(doc1);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
