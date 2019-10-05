package com.swpu.uchain.openexperiment.DTO;

import lombok.Data;

import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 */
@Data
public class AttachmentFileDTO {

    private Long fileId;
    private Integer downloadTimes;
    private String fileName;
    private Integer fileType;
    private String size;
    private Date uploadTime;
    private Long uploadUserId;
    private String userName;

}
