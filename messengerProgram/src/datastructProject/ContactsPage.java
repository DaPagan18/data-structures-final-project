package datastructProject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        JButton sortDefault = new JButton("Sort default");
        sortDefault.addActionListener(e -> refreshList(new ArrayList<>(contactManager.getAllContacts().values())));

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonBar.add(addContactButton);
        buttonBar.add(mostRecentlyActive);
        buttonBar.add(alphabeticalOrder);
        buttonBar.add(sortDefault);

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

        // Populate the list immediately (covers contacts loaded from a file)
        refreshList(new ArrayList<>(contactManager.getAllContacts().values()));
    }
    
    /** Adds a single row for the given contact into the scrollable list. */
    private void addContactRow(Contact contact) {
        JButton row = new JButton(contact.getName() + "  —  " + contact.getPhoneNumber());
        row.setHorizontalAlignment(SwingConstants.LEFT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        row.addActionListener(e -> openContactProfile(contact));

        contactListPanel.add(row);
        contactListPanel.revalidate();
        contactListPanel.repaint();
    }

    private void openContactProfile(Contact contact) {
        //not finished
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
            refreshList(new ArrayList<>(contactManager.getAllContacts().values()));
        }
    }


    private void sortByMostRecent() {
        // i will finish this once the message system has been implemented as that will have the time of message sent
    }

    private void sortByAlphabeticalOrder() {
        // Get all contacts, sort by name A→Z, then refresh the panel
        List<Contact> sorted = new ArrayList<>(contactManager.getAllContacts().values());
        sorted.sort(Comparator.comparing(c -> c.getName().toLowerCase()));
        refreshList(sorted);
    }

    /**
     * Clears the contact list panel and redraws it using the provided ordered list.
     */
    private void refreshList(List<Contact> contacts) {
        contactListPanel.removeAll();
        for (Contact contact : contacts) {
            addContactRow(contact);
        }
        contactListPanel.revalidate();
        contactListPanel.repaint();
    }
}

