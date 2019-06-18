package com.swpu.uchain.openexperiment.VO.file;

import com.swpu.uchain.openexperiment.VO.user.UserVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 */
@Data
public class AttachmentFileVO implements Serializable {
    private Long fileId;
    private Integer downloadTimes;
    private String fileName;
    private Integer fileType;
    private String size;
    private Date uploadTime;
    private UserVO uploadUser;
}
