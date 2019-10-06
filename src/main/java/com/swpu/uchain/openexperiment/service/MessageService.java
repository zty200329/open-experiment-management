package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.result.Result;

/**
 * @author panghu
 */
public interface MessageService {

    Result getAllMassage();

    Result readMessage(Long id);
}
