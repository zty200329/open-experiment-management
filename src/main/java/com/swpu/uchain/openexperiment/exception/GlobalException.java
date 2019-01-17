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

    public GlobalException(CodeMsg codeMsg) {
        super();
        this.codeMsg = codeMsg;
    }

}
