package com.swpu.uchain.openexperiment;

import java.util.Scanner;

public class Student {
    private String name;
    private String address;
    public void setAddress(String name) {
        if (!(name.contains("省") || name.contains("市"))) {
            throw new IllegalNameException();
        }
        this.name = name;
    }
    public void setName(String address) {
        if (address.length() < 1 || address.length() > 5){
            throw new IllegalNameException();
        }
        this.address = address;
    }

    public static void main(String[] args) {
        Student student= new Student();
        Scanner scanner = new Scanner(System.in);
        System.out.print("name:");
        String name = scanner.nextLine();
        student.setName(name);
        System.out.print("address:");
        String address = scanner.nextLine();
        student.setAddress(address);
    }
}
