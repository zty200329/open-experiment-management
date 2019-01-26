package com.swpu.uchain.openexperiment.form.announcement;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 */
@Data
public class AnnouncementUpdateForm extends AnnouncementPublishForm {
    @NotNull(message = "公告id不能为空")
    private Long announcementId;
}
