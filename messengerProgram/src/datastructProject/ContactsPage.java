/**
 * The contact page for the messenger program
 *
 * @author Calum Davies
 */
package datastructProject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContactsPage extends JPanel {



    /*  Panel that holds the individual contact rows inside the scroll pane*/
    private final JPanel contactListPanel = new JPanel();

    /*used for accessing the contacts profile page */
    private final CardLayout innerCards = new CardLayout();
    private final JPanel contentPanel = new JPanel(innerCards);
    private ContactProfilePage profilePanel;
    private final JLabel title = new JLabel("Your Contacts");
    private JPanel buttonBar;
    Profile profile;

    public ContactsPage(Profile profile) {
        this.profile = profile;

        setLayout(new BorderLayout());
    

        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JButton addContactButton = new JButton("Add Contact");
        addContactButton.addActionListener(e -> openAddContactDialog());

        JButton mostRecentlyActive = new JButton("Sort by most recently active");
        mostRecentlyActive.addActionListener(e -> sortByMostRecent());

        JButton alphabeticalOrder = new JButton("Sort alphabetically");
        alphabeticalOrder.addActionListener(e -> sortByAlphabeticalOrder());

        JButton sortDefault = new JButton("Sort default");
        sortDefault.addActionListener(e -> refreshList(new ArrayList<>(profile.getAllContacts().values())));

        buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonBar.add(addContactButton);
        buttonBar.add(mostRecentlyActive);
        buttonBar.add(alphabeticalOrder);
        buttonBar.add(sortDefault);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(buttonBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        contactListPanel.setLayout(new BoxLayout(contactListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contactListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        profilePanel = new ContactProfilePage(profile, () -> {
            title.setText("Your Contacts");
            buttonBar.setVisible(true);
            refreshList(new ArrayList<>(profile.getAllContacts().values()));
            innerCards.show(contentPanel, "list");
        });
        contentPanel.add(scrollPane, "list");
        contentPanel.add(profilePanel, "profile");
        add(contentPanel, BorderLayout.CENTER);

        refreshList(new ArrayList<>(profile.getAllContacts().values()));
    }
    
    /** Adds a single row for the given contact into the scrollable list. */
    private void addContactRow(Contact contact) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        rowPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JButton openButton = new JButton(contact.getName() + "  —  " + contact.getPhoneNumber());
        openButton.setHorizontalAlignment(SwingConstants.LEFT);
        openButton.addActionListener(e -> openContactProfile(contact));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteContact(contact));

        rowPanel.add(openButton, BorderLayout.CENTER);
        rowPanel.add(deleteButton, BorderLayout.EAST);

        contactListPanel.add(rowPanel);
        contactListPanel.revalidate();
        contactListPanel.repaint();
    }

    private void deleteContact(Contact contact) {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Delete " + contact.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            profile.removeContact(contact.getPhoneNumber());
            refreshList(new ArrayList<>(profile.getAllContacts().values()));
        }
    }

    private void openContactProfile(Contact contact) {
        title.setText(contact.getName() + "'s Profile");
        buttonBar.setVisible(false);        
        profilePanel.setPage(contact);
        innerCards.show(contentPanel, "profile");
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
            profile.addContact(newContact);
            refreshList(new ArrayList<>(profile.getAllContacts().values()));
        }
    }


    private void sortByMostRecent() {
        // i will finish this once the message system has been implemented as that will have the time of message sent
    }

    private void sortByAlphabeticalOrder() {
        // Get all contacts, sort by name A→Z, then refresh the panel
        List<Contact> sorted = new ArrayList<>(profile.getAllContacts().values());
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

