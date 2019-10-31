import javax.swing.*;
import java.awt.*;

public class ExperimentOne {

    public static void main(String[] args) {
        JFrame frame = new JFrame("计分器");
        frame.setSize(1280,960);
        frame.setVisible(true);
        frame.setLocation(100,100);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setSize(1280,40);
        panel.setLocation(0,0);

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.BLACK);
        panel2.setSize(10,10);
        panel2.setLocation(720,640);
        panel2.add(new TextField());

        frame.setLayout(null);
        frame.add(panel);
        frame.add(panel2);

        TextField textField1 = new TextField();
        textField1.setSize(10,15);
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        TextField textField4 = new TextField();
        TextField textField5 = new TextField();
        TextField textField6 = new TextField();
        TextField textField7 = new TextField();
        TextField textField8 = new TextField();

        GridLayout layout = new GridLayout(18,2);

//        panel.setLayout(layout);
//        panel.add(textField1);
//        panel.add(textField2);
//        panel.add(textField3);
//        panel.add(textField4);
//        panel.add(textField5);
//        panel.add(textField6);
//        panel.add(textField7);
//        panel.add(textField8);

    }

}
