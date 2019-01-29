package com.swpu.uchain.openexperiment.VO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMemberVO {
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
}
