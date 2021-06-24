package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;

public class HomepageAchievementIsTop implements Serializable {
    private Integer id;

    private Integer homepageAchievementId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHomepageAchievementId() {
        return homepageAchievementId;
    }

    public void setHomepageAchievementId(Integer homepageAchievementId) {
        this.homepageAchievementId = homepageAchievementId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", homepageAchievementId=").append(homepageAchievementId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}