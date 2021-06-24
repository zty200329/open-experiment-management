package com.swpu.uchain.openexperiment;

import java.util.Scanner;

public class ExceptionHandleDemo {

    public static void main(String[] args) throws IllegalAccessException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入一个数字：");
        int number = scanner.nextInt();
        System.out.println();
        if (number <= 0){
            throw new IllegalAccessException("数字不能为0或者负数");
        }
        long sum = 1;
        for (int i = 1; i <= number; i++) {
            sum *= i;
        }
        System.out.println(number+"!的结果是"+sum);

    }

}
