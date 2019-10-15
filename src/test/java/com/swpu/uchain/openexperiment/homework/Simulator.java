package com.swpu.uchain.openexperiment.homework;

public class Simulator {

    void playSound(Animal animal) {
        System.out.println("The crying animal`s name is" + animal.getAnimalName());
        animal.cry();
    }

}
