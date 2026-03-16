package datastructProject;

import javax.swing.*;

public class ContactsPage extends JPanel {
    public ContactsPage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(20));
        
        JLabel title = new JLabel("Your Contacts");
        title.setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 24));
        add(title);

    }
}
