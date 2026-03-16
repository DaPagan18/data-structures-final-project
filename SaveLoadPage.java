package datastructProject;

import javax.swing.*;

public class SaveLoadPage extends JPanel {
    public SaveLoadPage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(20));
        
        JLabel title = new JLabel("Save/Load");
        title.setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 24));
        add(title);

    }
}
