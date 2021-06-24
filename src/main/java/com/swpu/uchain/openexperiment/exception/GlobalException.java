package com.swpu.uchain.openexperiment.exception;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import lombok.Data;
/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 全局异常类
 */
@Data
public class GlobalException extends RuntimeException{
    private CodeMsg codeMsg;

    private String message;
    private Integer code;

    public GlobalException(CodeMsg codeMsg) {
        super();
        this.message = codeMsg.getMsg();
        this.code = codeMsg.getCode();
    }

    public GlobalException(String message,Integer code) {
        super();
        this.message = message;
        this.code = code;
    }





}
