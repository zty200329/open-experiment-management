package com.swpu.uchain.openexperiment.util;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateFile {

    public static void main(String[] args) throws IOException {
        File file = new File("/home/panghu/Desktop/测试.doc");
        if (!file.exists()) {
            boolean result = file.createNewFile();
            if (!result) {
                throw new FileSystemException("测试");
            }
        }
        OutputStream outputStream = new FileOutputStream(file);
        Files.copy(Paths.get("/home/panghu/Desktop/83_立项申请主要内容(复件).doc"), outputStream);
    }

}
