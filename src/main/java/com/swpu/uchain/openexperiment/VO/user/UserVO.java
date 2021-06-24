package com.swpu.uchain.openexperiment.VO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {

    private Long userId;
    private String userName;

}
