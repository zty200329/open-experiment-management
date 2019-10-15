package com.swpu.uchain.openexperiment.homework;

public class Dog  implements Animal {

    @Override
    public void cry() {
        System.out.println("wang wang wang ....");
    }

    @Override
    public String getAnimalName() {
        return "dog";
    }

}
