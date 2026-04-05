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

public class ContactsPage extends JPanel
{
    /*  Panel that holds the individual contact rows inside the scroll pane*/
    private final JPanel contactListPanel = new JPanel();

    /*used for accessing the contacts profile page */
    private final CardLayout innerCards = new CardLayout();
    private final JPanel contentPanel = new JPanel(innerCards);
    private ContactProfilePage profilePanel;
    private final JLabel title = new JLabel("Your Contacts");
    private JPanel buttonBar;

    private Profile profile;
    private UserRegistry userRegistry;


    // ### CONSTRUCTOR ### //
    /**
     * constructor for creating a ContactsPage instance.
     *
     * @param profile The profile of the user.
     * @param userRegistry The user registry for managing user information.
     */
    public ContactsPage(Profile profile, UserRegistry userRegistry) 
    {
        this.profile = profile;
        this.userRegistry = userRegistry;

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

        profilePanel = new ContactProfilePage(profile, NavigationManager.getInstance().getChatManager(), profile.getPhoneNumber(),  () -> {
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
    
    /** 
     * Private helper method to add a single row for the given contact into the scrollable list. 
     * 
     * @param contact The contact for which a row should be added to the list
    */
    private void addContactRow(Contact contact) 
    {
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

    /** 
     * Private helper method to delete the given contact after confirming with the user
     * 
     * @param contact The contact to be deleted
     */
    private void deleteContact(Contact contact) 
    {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Delete " + contact.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) 
            {
            profile.removeContact(contact.getPhoneNumber());
            refreshList(new ArrayList<>(profile.getAllContacts().values()));
        }
    }

    /**
     * Private helper method that opens the contact profile page for the given contact, hiding the contact list
     * 
     * @param contact The contact whose profile page should be opened
     */
    private void openContactProfile(Contact contact) 
    {
        title.setText(contact.getName() + "'s Profile");
        buttonBar.setVisible(false);        
        profilePanel.setPage(contact);
        innerCards.show(contentPanel, "profile");
    }

    /**
     * Private helper method to open a dialog asking the user for a name and phone number,
     * then creates a Contact, adds it to the contact manager, and
     * appends a row to the scrollable list.
     */
    private void openAddContactDialog() 
    {
        JTextField phoneField = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(1, 1, 8, 8));
        form.add(new JLabel("Phone Number:"));
        form.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add New Contact",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) 
            {
            String phone = phoneField.getText().trim();

            if (!userRegistry.lookup(phone))
                {
                    JOptionPane.showMessageDialog(this, "Sorry this user does not exist inside the registry of users");
                    return;
                }

                if (phone.isEmpty()) 
                    {
                        JOptionPane.showMessageDialog(this, "phone number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

            Profile registeredUser = userRegistry.getProfile(phone);
            String picPath = registeredUser.getProfilePicPath();

            // Create a new contact and add it to the profile
            Contact newContact = new Contact(phone, picPath);
            profile.addContact(newContact);
            refreshList(new ArrayList<>(profile.getAllContacts().values()));
        }
    }


    /**
     * Sorts the contact list by the time of the most recent message received,
     * with the most recently active contact appearing first.
     * Contacts with no messages are placed at the bottom.
     */
    private void sortByMostRecent() 
    {
        ChatManager chatManager = NavigationManager.getInstance().getChatManager();
        List<Contact> sorted = new ArrayList<>(profile.getAllContacts().values());

        sorted.sort((a, b) -> {
            java.time.LocalDateTime timeA = null;
            java.time.LocalDateTime timeB = null;

            // If statement to check if there is a chat with contact A
            if (chatManager.checkForChat(profile.getPhoneNumber(), a.getPhoneNumber())) 
                {
                    // If there is a chat, get the last message and its time sent
                    Chat chatA = chatManager.getOrCreateChatForContact(profile.getPhoneNumber(), a.getPhoneNumber());
                    Message lastA = chatA.getLastMessage();
                    // If there is a last message, get its time sent
                    if (lastA != null) 
                        {
                            timeA = lastA.getTimeSent();
                        }
                }

            // If statement to check if there is a chat with contact B
            if (chatManager.checkForChat(profile.getPhoneNumber(), b.getPhoneNumber())) 
                {
                    // If there is a chat, get the last message and its time sent
                    Chat chatB = chatManager.getOrCreateChatForContact(profile.getPhoneNumber(), b.getPhoneNumber());
                    Message lastB = chatB.getLastMessage();
                    // If there is a last message, get its time sent
                    if (lastB != null) 
                        {
                            timeB = lastB.getTimeSent();
                        }
                }

            // If statement to handle cases where one or both contacts have no messages
            if (timeA == null && timeB == null) 
                {
                    return 0;
                }
                if (timeA == null) 
                    {
                        return 1;
                    }
                    if (timeB == null) 
                        {
                            return -1;
                        }
            // If both contacts have messages, sort by most recent time (descending order)
            return timeB.compareTo(timeA);
        });

        // After sorting, refresh the list to update the display
        refreshList(sorted);
    }

    /**
     * Sorts the contact list alphabetically by contact name, from A to Z
     */
    private void sortByAlphabeticalOrder() 
    {
        // Get all contacts, sort by name A to Z, then refresh the panel
        List<Contact> sorted = new ArrayList<>(profile.getAllContacts().values());
        sorted.sort(Comparator.comparing(c -> c.getName().toLowerCase()));
        refreshList(sorted);
    }

    /**
     * Clears the contact list panel and redraws it using the provided ordered list
     * 
     * @param contacts The list of contacts to display, in the order they should be displayed in
     */
    private void refreshList(List<Contact> contacts) 
    {
        contactListPanel.removeAll();
        // Iterate through the list of contacts and add a row for each one
        for (Contact contact : contacts) 
            {
                addContactRow(contact);
            }
        contactListPanel.revalidate();
        contactListPanel.repaint();
    }

    /**
     * Method to refresh the contact list
     */
    public void refresh() 
    {
        refreshList(new ArrayList<>(profile.getAllContacts().values()));
    }
}

