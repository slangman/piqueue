package kz.hustle.equeue;

import javax.swing.*;

public class SwingApp {

    private JFrame frame;
    private JLabel label;

    //@EventListener(ApplicationReadyEvent.class)
    public void createAndShowGUI() {

        // Create new window (JFrame)
        frame = new JFrame("Spring Boot + Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Add simple JLabel
        label = new JLabel("Hello, Swing with Spring Boot!", JLabel.CENTER);
        frame.getContentPane().add(label);

        // Show window
        frame.setVisible(true);
    }

    public void updateLabel(String text) {
        label.setText(text);
    }

}
