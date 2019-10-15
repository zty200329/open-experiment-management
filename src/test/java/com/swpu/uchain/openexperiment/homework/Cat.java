package com.swpu.uchain.openexperiment.homework;


public class Cat implements Animal {

    @Override
    public void cry() {
        System.out.println("miao miao miao ...");
    }

    @Override
    public String getAnimalName() {
        return "cat";
    }


}
