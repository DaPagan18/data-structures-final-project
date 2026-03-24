package datastructProject;

import javax.swing.*;
import java.awt.*;

public class ContactProfilePage extends JPanel {

    private final JLabel phoneLabel = new JLabel();

    public ContactProfilePage(Runnable onBack) {
        setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
    }

    public void setPage(Contact contact) {
        phoneLabel.setText("Phone: " + contact.getPhoneNumber());
    }

}
