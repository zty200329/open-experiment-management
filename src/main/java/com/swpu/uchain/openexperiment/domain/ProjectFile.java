package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dengg
 */
@Data
public class ProjectFile implements Serializable {
    private Long id;

    private Long projectGroupId;

    private Integer downloadTimes;

    private String fileName;

    /**
     *  {@link com.swpu.uchain.openexperiment.enums.FileType}
     */
    private Integer fileType;

    private String size;

    private Date uploadTime;

    private Long uploadUserId;

    private static final long serialVersionUID = 1L;
}