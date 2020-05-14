package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.result.Result;

/**
 * @author panghu
 */
public interface MessageService {

    Result getAllMassage();

    Result readMessage(Long id);

    /**
     * 获取当前用户未读消息
     * @return
     */
    Result getMessageTips();
}
