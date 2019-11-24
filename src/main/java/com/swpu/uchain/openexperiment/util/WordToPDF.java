package com.swpu.uchain.openexperiment.util;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * 存在合适问题
 */
public class WordToPDF {


    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        String docPath = "/home/panghu/Desktop/装换测试文档.docx";
        String pdfPath = "/home/panghu/Desktop/装换测试文档.pdf";
        XWPFDocument document;
        try (InputStream doc = Files.newInputStream(Paths.get(docPath))) {
            document = new XWPFDocument(doc);
        }
        PdfOptions options = PdfOptions.create();
        try (OutputStream out = Files.newOutputStream(Paths.get(pdfPath))) {
            PdfConverter.getInstance().convert(document, out, options);
        }

    }

}
