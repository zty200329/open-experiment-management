package com.swpu.uchain.openexperiment;

import javax.swing.*;
import java.awt.*;

public class GridPanel  extends JPanel {

    private int result;

    public int getResult() {
        return result;
    }

    public GridPanel(int locationY,String text) {
        this.setSize(1280,20);
        this.setVisible(true);
        this.setLayout(null);
        this.setLocation(0,locationY);

        int locationX = 0;
        for (int i = 0; i < 8 ;i++){
            JTextField jTextField = new JTextField();
            jTextField.setSize(60,18);
            this.add(jTextField);
            locationX = 80+80*i;
            jTextField.setLocation(locationX,locationY+2);
        }
        JButton confirmButton = new JButton();
        confirmButton.setBackground(Color.YELLOW);
        confirmButton.setSize(100,20);
        confirmButton.setLocation(locationX+100,locationY);
        confirmButton.setText("提交成绩");

        JLabel label = new JLabel();
        label.setText(text);
        Font f = new Font("黑体",Font.BOLD,12);
        label.setFont(f);
        label.setSize(80,20);
        label.setLocation(0,locationY);
        this.add(label);
        this.add(confirmButton);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("计分器");

        frame.setSize(1280,960);
        frame.setVisible(true);
        frame.setLocation(100,100);
        frame.setResizable(false);
        for (int i = 0; i < 20; i++) {
            frame.add(new GridPanel(i*20,"选手"+i+1));
        }
        frame.setBackground(Color.LIGHT_GRAY);
    }
}
