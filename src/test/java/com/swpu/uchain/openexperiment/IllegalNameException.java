package com.swpu.uchain.openexperiment;

public class IllegalNameException extends RuntimeException {

    public IllegalNameException() {
        super("非法名称");
    }
}
