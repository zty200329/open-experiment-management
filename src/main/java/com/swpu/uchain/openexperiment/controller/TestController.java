//package com.swpu.uchain.openexperiment.controller;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.swpu.uchain.openexperiment.mapper.ProjectGroupMapper;
//import com.swpu.uchain.openexperiment.result.Result;
//import io.swagger.annotations.Api;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
///**
// * @author panghu
// */
//@Api
//@Slf4j
//@RestController
//@RequestMapping("/api/test")
//public class TestController {
//
//    @Autowired
//    private ProjectGroupMapper projectGroupMapper;
//
//    @GetMapping("/getProjectGroupDetailVOByProjectId")
//    public Object getProjectGroupDetailVOByProjectId(){
//        return Result.success(projectGroupMapper.getProjectGroupDetailVOByProjectId(1L));
//    }
//
//    String realPath = "/home/panghu/Desktop/83_立项申请主要内容.pdf";
//
//    public static boolean downloadFile(HttpServletResponse response, String realPath) {
//        File file = new File(realPath);
//        //如果文件不存在
//        if (!file.exists()) {
//            log.error("文件 " + realPath + " 不存在!");
//            return true;
//        }
//        String simpleName = file.getName().substring(file.getName().lastIndexOf("/") + 1);
//        String newFileName = new String(simpleName.getBytes(), StandardCharsets.UTF_8);
//        //不设置这个的话，有可能会乱码
//        response.setContentType("application/force-download");
//        try {
//            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(newFileName,"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        byte[] buffer = new byte[1024];
//        //文件输入流
//        FileInputStream fis = null;
//        BufferedInputStream bis = null;
//
//        //输出流
//        OutputStream os = null;
//        try {
//            os = response.getOutputStream();
//            writeStringToOutputStreamAsPDF("/home/panghu/Desktop/83_立项申请主要内容.html",os);
//            fis = new FileInputStream(file);
//            bis = new BufferedInputStream(fis);
//            int i = bis.read(buffer);
//            while (i != -1) {
//                os.write(buffer);
//                i = bis.read(buffer);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        log.info("文件下载中........");
//        try {
//            assert bis != null;
//            bis.close();
//            fis.close();
//            assert os != null;
//            os.close();
//        } catch (IOException e) {
//            log.error("error:{}", e.getMessage());
//        }
//        log.info("文件下载完成");
//        return false;
//    }
//
//    public static void writeStringToOutputStreamAsPDF(String html, OutputStream os) {
//        writeToOutputStreamAsPDF(new ByteArrayInputStream(html.getBytes()), os);
//    }
//
//    public static void writeToOutputStreamAsPDF(InputStream html, OutputStream os) {
//        try {
//            Document document = new Document(PageSize.A4);
//            PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
//            document.open();
//            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
//            worker.parseXHtml(pdfWriter, document, html, StandardCharsets.UTF_8, new AsianFontProvider());
//            document.close();
//        } catch (Exception e) {
//        }
//    }
//}
///**
// * 用于中文显示的Provider
// */
//class AsianFontProvider extends XMLWorkerFontProvider {
//    @Override
//    public Font getFont(final String fontname, String encoding, float size, final int style) {
//        try {
//            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            return new Font(bfChinese, size, style);
//        } catch (Exception e) {
//        }
//        return super.getFont(fontname, encoding, size, style);
//    }
//
//    @Test
//    public  void readAndWriterTest4() {
//        File file = new File("C:\\Users\\dengg\\Desktop\\开放性实验\\说明.docx");
//        try {
//            FileInputStream fis = new FileInputStream(file);
//            XWPFDocument xdoc = new XWPFDocument(fis);
//            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
//            String doc1 = extractor.getText();
//            System.out.println(doc1);
//            fis.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
