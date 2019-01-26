package com.swpu.uchain.openexperiment.util;

/**
 * @Description 判断文件类型工具类
 * 1：excel  2：word  3 video媒体类文件 4：图片类文件
 * @Author cby
 * @Date 19-1-22
 **/
public class FileTypeUtil {



    public int getType(String suffix) {
        if (suffix.equals(".xls")) {
            return 1;
        }
        if (suffix.equals(".xlsx")) {
            return 1;
        }
        if (suffix.equals(".doc")) {
            return 2;
        }
        if (suffix.equals(".docx")) {
            return 2;
        }
        if (suffix.equals(".mp3")) {
            return 3;
        }
        if (suffix.equals(".mp4")) {
            return 3;
        }

        if (suffix.equals(".jpg")) {
            return 4;
        }
        if (suffix.equals(".png")) {
            return 4;
        }
        if (suffix.equals(".img")) {
            return 4;
        }
        return 0;
    }

}
