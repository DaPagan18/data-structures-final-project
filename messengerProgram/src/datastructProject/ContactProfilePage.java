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

        JPanel phoneRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phoneRow.add(phoneLabel);
        JButton editPhoneButton = new JButton("Edit");
        editPhoneButton.addActionListener(e -> editPhoneNumber());
        phoneRow.add(editPhoneButton);
        add(phoneRow, BorderLayout.CENTER);
    }

    public void setPage(Contact contact) {
        currentContact = contact;
        phoneLabel.setText("Phone: " + contact.getPhoneNumber());
        revalidate();
        repaint();
    }

    private void editPhoneNumber() {
        if (currentContact == null) return;

        String oldPhone = currentContact.getPhoneNumber();
        String input = JOptionPane.showInputDialog(this, "Enter new phone number:", oldPhone);
        if (input == null) return;

        String newPhone = input.trim();
        if (newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!oldPhone.equals(newPhone) && profile.hasContact(newPhone)) {
            JOptionPane.showMessageDialog(this, "That phone number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        profile.removeContact(oldPhone);
        currentContact.setPhoneNumber(newPhone);
        profile.addContact(currentContact);
        phoneLabel.setText("Phone: " + newPhone);
        revalidate();
        repaint();
    }

}
