package datastructProject;

import javax.swing.*;

public class EditProfilePage extends JPanel {
    public EditProfilePage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(20));
        
        JLabel title = new JLabel("Edit Profile");
        title.setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 24));
        add(title);
    }
}
