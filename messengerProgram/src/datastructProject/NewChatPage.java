package datastructProject;

import javax.swing.*;

public class NewChatPage extends JPanel {
    public NewChatPage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(20));
        
        JLabel title = new JLabel("Start New Chat");
        title.setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 24));
        add(title);

    }
}
