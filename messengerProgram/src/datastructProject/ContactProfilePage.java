package datastructProject;

import javax.swing.*;
import java.awt.*;

public class ContactProfilePage extends JPanel {

    private final JLabel phoneLabel = new JLabel();
    private final Profile profile;
    private Contact currentContact;

    public ContactProfilePage(Profile profile, Runnable onBack) {
        this.profile = profile;
        setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

    }

    public void setPage(Contact contact) {
        currentContact = contact;
        phoneLabel.setText("Phone: " + contact.getPhoneNumber());
        revalidate();
        repaint();
    }

  

}
