package com.swpu.uchain.openexperiment.file;

import com.swpu.uchain.openexperiment.service.xdoc.XDocService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: clf
 * @Date: 19-1-26
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FileTest {
    @Autowired
    private XDocService xDocService;

    @Test
    public void convertTest(){
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("test", "测试param");
            param.put("test2", "hello");
            param.put("test3", "world");
            param.put("test4", "我");
            xDocService.run("/home/hk/testFile/test.xlsx",
                    param,
                    new File("/home/hk/testFile/testConvert.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
