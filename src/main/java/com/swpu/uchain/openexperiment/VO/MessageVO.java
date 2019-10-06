package com.swpu.uchain.openexperiment.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author panghu
 */
@Data
public class MessageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 是否已读
     */
    private String readStatus;

    /**
     * 内容
     */
    private String title;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime;

    /**
     * 具体原因
     */
    private String content;

}
