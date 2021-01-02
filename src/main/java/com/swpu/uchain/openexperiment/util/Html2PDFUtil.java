package com.swpu.uchain.openexperiment.util;


import java.io.*;

import com.itextpdf.text.DocumentException;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.lowagie.text.pdf.BaseFont;

@Slf4j
public class Html2PDFUtil {

    //
    public static void main(String[] args) {
        convertHtml2PDF("/Users/zty/Desktop/openexperiment/394_立项申请主要内容.html",
                "/home/zty/open-experiment-management-system/apply_dir_2/454515154_立项申请主要内容.pdf");
    }

    public static void convertHtml2PDF(String htmlPath,String pdfPath) {
        File file = new File(htmlPath);
        try {
            //读取html的流
            InputStream inputStream = new FileInputStream(file);

            //流转换成字符串
            StringBuilder out = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = inputStream.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }

            String html = out.toString();

            OutputStream os = new FileOutputStream(pdfPath);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            // writer.setPageEvent(header);
            ITextFontResolver fontResolver = renderer.getFontResolver();
            // 添加字体支持,路径可以自身项目的实际情况设置，我这里是本地项目，而且为了方便测试，就写成固定的了
            // 实际项目中，可以获取改字体所在真实的服务器的路径,这个方法是本地地址和网络地址都支持的
            // 这里面添加的是宋体
//            fontResolver.addFont("/usr/share/fonts/chinese/simsun.ttc",
            fontResolver.addFont("/usr/share/fonts/simsun.ttc",
//            fontResolver.addFont("/Users/zty/Desktop/openexperiment/simsun.ttc",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);


            renderer.layout();
            renderer.createPDF(os);
            os.flush();
            os.close();
        } catch (IOException | DocumentException e){
            throw new GlobalException(CodeMsg.PDF_CONVERT_ERROR);
        }
    }

}
