package com.swpu.uchain.openexperiment.VO.project;

import lombok.Data;

/**
 * @author zty
 * @date 2020/5/28 下午4:47
 * @description: 项目附件
 */
@Data
public class ProjectAnnex {

    private Long id;

    /**
     * 附件类型 参照FileType
     */
    private Integer fileType;

    /**
     * 上传者学号
     */
    private Long uploadUserId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 附件url（音视频）
     */
    private String url;
}
