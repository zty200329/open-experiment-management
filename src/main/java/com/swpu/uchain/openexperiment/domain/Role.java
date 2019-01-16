package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-1-16
 * @Description:
 * 角色
 */
@Data
public class Role {
    /**角色自增id主键*/
    @Id
    @GeneratedValue
    private Long id;
    /**角色名*/
    private String name;
    /**创建时间*/
    private Date createTime;
    /**更新时间*/
    private Date updateTime;
}
