package com.swpu.uchain.openexperiment.VO.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMemberVO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 成员角色
     */
    private Integer memberRole;

    private String major;

    private String grade;

    private String phone;

    public UserMemberVO(Long userId, String userName, Integer memberRole, String major, String grade) {
        this.userId = userId;
        this.userName = userName;
        this.memberRole = memberRole;
        this.major = major;
        this.grade = grade;
    }
}
