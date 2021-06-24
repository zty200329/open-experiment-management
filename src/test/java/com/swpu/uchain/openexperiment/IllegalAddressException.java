package com.swpu.uchain.openexperiment;

public class IllegalAddressException extends RuntimeException {

    public IllegalAddressException() {
        super("非法地址");
    }
}
