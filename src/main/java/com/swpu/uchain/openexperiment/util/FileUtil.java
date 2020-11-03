package com.swpu.uchain.openexperiment.util;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.FileType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

/**
 * @Description 文件工具类
 * @Author cby
 * @Date 19-1-22
 **/
@Slf4j
public class FileUtil {


    /**
     * 获取文件类型
     *
     * @param suffix
     * @return
     */
    public static int getType(String suffix) {
        if (".xls".equals(suffix)) {
            return FileType.EXCEL.getValue();
        }
        if (".xlsx".equals(suffix)) {
            return FileType.EXCEL.getValue();
        }
        if (".doc".equals(suffix)) {
            return FileType.WORD.getValue();
        }
        if (".docx".equals(suffix)) {
            return FileType.WORD.getValue();
        }
        if (".mp3".equals(suffix)) {
            return FileType.VIDEO.getValue();
        }
        if (".mp4".equals(suffix)) {
            return FileType.VIDEO.getValue();
        }

        if (".jpg".equals(suffix)) {
            return FileType.IMAGE.getValue();
        }
        if (".png".equals(suffix)) {
            return FileType.IMAGE.getValue();
        }
        if (".img".equals(suffix)) {
            return FileType.IMAGE.getValue();
        }
        if (".zip".equals(suffix)) {
            return FileType.ZIP.getValue();
        }
        return 0;
    }

    /**
     * 上传文件
     *
     * @param multipartFile
     * @param realPath
     * @return
     */
    public static boolean uploadFile(MultipartFile multipartFile, String realPath) {
        try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(realPath);
            Files.write(path, bytes);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 下载文件
     *
     * @param response
     * @param realPath
     * @return
     */
    public static boolean downloadFile(HttpServletResponse response, String realPath) {
        File file = new File(realPath);
        //如果文件不存在
        if (!file.exists()) {
            log.error("文件 " + realPath + " 不存在!");
            return true;
        }
        String simpleName = file.getName().substring(file.getName().lastIndexOf("/") + 1);
        String newFileName = new String(simpleName.getBytes(), StandardCharsets.UTF_8);
        //不设置这个的话，有可能会乱码
        response.setContentType("application/force-download");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(newFileName,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[1024];
        //文件输入流
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        //输出流
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("文件下载中........");
        try {
            assert bis != null;
            bis.close();
            fis.close();
            assert os != null;
            os.close();
        } catch (IOException e) {
            log.error("error:{}", e.getMessage());
        }
        log.info("文件下载完成");
        return false;
    }

    /**
     * 获取文件真正的路径
     * @param fileId
     * @param uploadDir
     * @param fileName
     * @return
     */
    public static String getFileRealPath(Long fileId, String uploadDir, String fileName){
        return uploadDir + "/" + fileId + "_" + fileName;
    }
    /**
     * 获取文件真正的路径
     * @param uploadDir
     * @param fileName
     * @return
     */
    public static String getFileRealPath(String uploadDir, String fileName){
        return uploadDir + "/"+fileName;
    }
    /**
     * 获取文件的大小
     * @param fileSize
     * @return
     */
    public static String FormatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取不带后缀的文件名
     * @param fileName
     * @return
     */
    public static String getFileNameWithoutSuffix(String fileName){
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 获取上传文件的后缀
     * @param multipartFile
     * @return
     */
    public static String getMultipartFileSuffix(MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }


    /**
     * 删除指定文件
     * @param realPath
     * @return
     */
    public static boolean deleteFile(String realPath) {
        File file = new File(realPath);
        if (!file.exists()){
            return true;
        }
        if (file.delete()) {
            return true;
        }
        return false;
    }

    /**
     * 获取文件后缀名
     * @param fileName 文件名
     * @return
     */
    public static String getFileSuffix(String fileName){
        if (fileName == null){
            throw new GlobalException(CodeMsg.FILE_NAME_EMPTY_ERROR);
        }
        //最后一位  注意是"\\",主要针对于微软的浏览器
        int lastIndexOfSlash = fileName.lastIndexOf(".");
        return fileName.substring(lastIndexOfSlash);
    }
}
