package datastructProject;

import javax.swing.*;
import java.awt.*;

public class ContactsPage extends JPanel {

    private final ContactManager contactManager = new ContactManager();

    // Panel that holds the individual contact rows inside the scroll pane
    private final JPanel contactListPanel = new JPanel();

    public ContactsPage() {
        setLayout(new BorderLayout());

        // --- Title + buttons at the top ---
        JLabel title = new JLabel("Your Contacts");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JButton addContactButton = new JButton("Add Contact");
        addContactButton.addActionListener(e -> openAddContactDialog());

        JButton mostRecentlyActive = new JButton("Sort by most recently active");
        mostRecentlyActive.addActionListener(e -> sortByMostRecent());

        JButton alphabeticalOrder = new JButton("Sort alphabetically");
        alphabeticalOrder.addActionListener(e -> sortByAlphabeticalOrder());

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonBar.add(addContactButton);
        buttonBar.add(mostRecentlyActive);
        buttonBar.add(alphabeticalOrder);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(buttonBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // --- Scrollable contact list in the centre ---
        contactListPanel.setLayout(new BoxLayout(contactListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contactListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /** Adds a single row for the given contact into the scrollable list. */
    private void addContactRow(Contact contact) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel nameLabel = new JLabel(contact.getName());
        nameLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel phoneLabel = new JLabel(contact.getPhoneNumber());
        phoneLabel.setForeground(Color.GRAY);
        phoneLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        row.add(nameLabel, BorderLayout.WEST);
        row.add(phoneLabel, BorderLayout.EAST);

        contactListPanel.add(row);
        contactListPanel.revalidate();
        contactListPanel.repaint();
    }

    /**
     * Opens a dialog asking the user for a name and phone number,
     * then creates a Contact, adds it to the contact manager, and
     * appends a row to the scrollable list.
     */
    private void openAddContactDialog() {
        JTextField nameField = new JTextField(20);
        JTextField phoneField = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Phone Number:"));
        form.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add New Contact",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and phone number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Contact newContact = new Contact(name, phone, "");
            contactManager.addContact(newContact);
            addContactRow(newContact);
        }
    }

    private void sortByMostRecent(){

    }

    private void sortByAlphabeticalOrder(){

    }
}

