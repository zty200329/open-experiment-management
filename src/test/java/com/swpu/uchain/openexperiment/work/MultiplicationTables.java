package com.swpu.uchain.openexperiment.work;

public class MultiplicationTables {

    private static void printTables(){
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print( " | " + j + " x " + i + " = " + i*j);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        printTables();
    }

}
