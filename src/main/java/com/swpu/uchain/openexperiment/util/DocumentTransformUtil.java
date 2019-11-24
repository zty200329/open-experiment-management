package com.swpu.uchain.openexperiment.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.StringJoiner;

@Slf4j
public class DocumentTransformUtil {

    /**
     * doc--->html
     * @param docFile  doc文件
     * @return  html文本
     */
    public static String doc2Html(File docFile) {
        String htmlPath=docFile.getAbsolutePath().replaceAll(docFile.getName(),"")+docFile.getName().replaceAll(".doc",".html");

        String result = "";
        File htmlFile = new File(htmlPath);
        if(!docFile.exists()){
            log.error("{}.doc不存在",docFile.getName());
        }
        try{
            HWPFDocument wordDocument = new HWPFDocument(new POIFSFileSystem(docFile));
            org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
            //保存图片，并返回图片的相对路径
            wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
                //图片byte[],图片type,图片名,图片宽度,图片高度
                //上传文件返回url  伪代码
                //String url =  FileUploadUtil.upload(fileByte, name, true);
                String url = "https://gss2.bdstatic.com/-fo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=bcd0f6384290f60304b09b410129d426/91ef76c6a7efce1bab44b2c3a751f3deb48f654f.jpg";
                return url;
            });
            wordToHtmlConverter.processDocument(wordDocument);
            org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult();
            streamResult.setOutputStream(new FileOutputStream(htmlFile));
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            result = FileUtils.readFileToString(htmlFile,"UTF-8");
            //这里拿到html文本后 jsoup 解析
            result =  processUeditorStyle(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("doc--->html出错:{}",e.getMessage());
        }finally {
            htmlFile.delete();
        }
        return result;
    }

    /**
     *  处理 ueditor 样式
     * @param result
     * @return
     */
    private static String processUeditorStyle(String result) {
        Document doc = Jsoup.parse(result);
        //body 处理
        //<body class="view" contenteditable="true" spellcheck="false" style="overflow-y: hidden; height: 500px; cursor: text;">
        doc.body().attr("class","view").attr("contenteditable","true")
                .attr("spellcheck","false").attr("style","overflow-y: hidden; height: 500px; cursor: text;");
        //img处理
        Elements imgs = doc.select("img[src]");
        for (Element img : imgs) {
            String width = "";
            String[] styles = img.attr("style").split(";");
            if (styles.length>0){
                width = styles[0].split(":")[1].replaceAll("in","");
                if (Float.parseFloat(width) > 7.43f){
                    StringJoiner styleValue = new StringJoiner(";");
                    styleValue.add("width:173px");
                    for (int i = 1; i < styles.length; i++) {
                        styleValue.add(styles[i]);
                    }
                    img.attr("style",styleValue.toString());
                }
            }
            if (StringUtils.isBlank(width)){
                //7.43英寸  doc 1920
                width = img.attr("width").replaceAll("px","");
                if (Integer.parseInt(width) > 713 ){
                    img.attr("width","713px");
                }
            }
            result = doc.outerHtml();
        }
        return result;
    }

    /**
     *  处理 doc 样式
     * @param htmlContent
     * @return
     */
    private static String processDocStyle(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        doc.select("table").attr("cellspacing","0px")
                .attr("cellpadding","0px").attr("border-collapse","collapse");
        return doc.outerHtml();
    }

    /**
     *  html转doc
     * @param file  输出doc文件
     * @param html  html文本内容
     * @throws Exception
     */
    public static void html2doc(File file, String html)  {
        log.info("开始html--->doc");
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            String table = "<style type=\"text/css\"> table td{border:1px solid #000000} </style>";
            String htmlContent = "<html><head><title></title>"+table+"</head><body>"+html+"</body></html>";
            htmlContent = processDocStyle(htmlContent);
            wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Html, htmlContent.getBytes());
            wordMLPackage.save(file);
            //上传至文件服务器  删除零时文件 .. 将此返回值改为String url入库
//            String url =  FileUploadUtil.upload(fileByte, name, true);
        } catch (Docx4JException e) {
            e.printStackTrace();
            log.error("html转doc出错:{}",e.getMessage());
        }
        log.info("转换完成html--->doc");

    }

    public static void main(String[] args) throws Exception {
//        doc2Html(new File("c:/优化规则.doc"));
        html2doc(new File("/home/panghu/Desktop/83_立项申请主要内容(复件).doc"),FileUtils.readFileToString(new File("/home/panghu/Desktop/83_立项申请主要内容(复件).html"),"UTF-8"));
    }
}
