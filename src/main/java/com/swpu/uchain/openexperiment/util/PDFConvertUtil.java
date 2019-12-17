package com.swpu.uchain.openexperiment.util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * 可行
 * @author dengg
 */
@Slf4j
public class PDFConvertUtil {

    public static void convert(String input, String output){
        File inputFile = new File(input);
        File outputFile = new File(output);
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(inputFile, outputFile);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{ if(connection != null){connection.disconnect();
            }
            }catch(Exception e){}
        }
    }

    // 将word格式的文件转换为pdf格式
    public static void Word2Pdf(String srcPath, String desPath) throws IOException {
        // 源文件目录
        File inputFile = new File(srcPath);
        if (!inputFile.exists()) {
            System.out.println("源文件不存在！");
            return;
        }
        // 输出文件目录
        File outputFile = new File(desPath);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().exists();
        }


        // window 使用  调用openoffice服务线程     本机C盘！！！
//        String command = "C:\\Program Files (x86)\\OpenOffice 4\\program\\soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp; -nofirststartwizard\"";
        //Linux使用
//           String command = "/opt/openoffice4/program/soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
//        Process p = Runtime.getRuntime().exec(command);

        // 连接openoffice服务
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(
                "127.0.0.1", 8100);
        connection.connect();
        System.out.println("已连接服务");

        // 转换word到pdf
        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(
                connection);
        converter.convert(inputFile, outputFile);
        // 关闭连接
        connection.disconnect();

        // 关闭进程
//        p.destroy();
        System.out.println("转换完成！");
    }

}
