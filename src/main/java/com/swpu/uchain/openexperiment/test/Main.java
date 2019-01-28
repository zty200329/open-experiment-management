package com.swpu.uchain.openexperiment.test;

import com.swpu.uchain.openexperiment.service.xdoc.XDocService;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-1-26
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        XDocService xDocService = new XDocService();
        try {
            Announcement announcement = new Announcement();
            announcement.title = "通知";
            announcement.content = "  测试一个通知模板是否可以正常使用";
            announcement.senderName = "柴林枫";
            announcement.date = new Date();
            xDocService.run(announcement, new File("/home/hk/testFile/ann.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @XDocService.XDoc("/home/hk/testFile/announcement.docx")
    public static class Announcement{
        @XDocService.XParam("标题")
        public String title;
        @XDocService.XParam("正文")
        public String content;
        @XDocService.XParam("发布人")
        public String senderName;
        @XDocService.XParam("日期")
        public Date date;

    }
}
