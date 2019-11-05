package com.swpu.uchain.openexperiment.util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import java.io.File;

/**
 * @author dengg
 */
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

}
