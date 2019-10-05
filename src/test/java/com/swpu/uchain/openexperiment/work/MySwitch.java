package com.swpu.uchain.openexperiment.work;

import java.util.Scanner;

public class MySwitch {

    private static void getGradeLevel(float score){
        if(score<60 && score >= 0)
            System.out.println("等级为：不及格");
        else if(score>=60 && score<70)
            System.out.println("等级为：及格");
        else if(score>=70 && score<80)
            System.out.println("等级为：中");
        else if(score>=80 && score<90)
            System.out.println("等级为：良");
        else if(score>=90 && score<=100)
            System.out.println("等级为：优");
        else System.out.println("输入成绩有误");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你的成绩并且以回车键作为结束:");
        float score = scanner.nextFloat();
        getGradeLevel(score);
    }

}
