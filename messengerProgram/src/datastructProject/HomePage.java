package datastructProject;

import javax.swing.*;

public class HomePage extends JPanel {
    public HomePage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(20));
        
        JLabel title = new JLabel("Welcome to Home!");
        title.setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 24));
        add(title);
    }
}
