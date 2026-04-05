/**
 * Class that creates and handles the main menu of the messenger program
 * 
 * @author Stuart Baxter
 */
package datastructProject;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class Menu 
{
    private final JFrame frame;
    private final ChatManager chatManager;
    private CardLayout cards;

    private final UserRegistry userRegistry = new UserRegistry();
    private Profile profile = new Profile(userRegistry);
    
    // Adding test profiles to the user registry to simulate another user being on the system
    private Profile testProfile = new Profile("Calum", "075", "messengerProgram/src/datastructProject/images/calumProfilePic.png", userRegistry);
    private Profile testProfile2 = new Profile("Daniel","123","", userRegistry);
    private Profile testProfile3 = new Profile("CalumS","456","", userRegistry);
    
    // ### CONSTRUCTOR ### //
    /**
     * constructor for creating a Menu instance.
     *
     * @throws IOException If there is an error loading resources.
     */
    public Menu() throws IOException 
    {
        frame = new JFrame("Text");

        frame.setSize(1000, 1000);
        frame.setTitle("Messenger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel title = new JLabel("Messenger");
        title.setFont(new Font("Sans Serif", Font.BOLD, 36));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setBounds(0, 0, 1000, 80);
        frame.add(title);

        // Create ChatManager
        this.chatManager = new ChatManager();

        // Dropdown menu options
        String[] pages = {"Home", "Contacts", "Search", "Edit Profile", "Save/Load"};
        JComboBox<String> dropdown = new JComboBox<>(pages);

        cards = new CardLayout();
        JPanel cardPanel = new JPanel(cards);
        NavigationManager.getInstance().register(cards, cardPanel);
        NavigationManager.getInstance().storeChatManager(chatManager);
        NavigationManager.getInstance().storeUserRegistry(userRegistry, profile.getPhoneNumber());

        JPanel homePanel = new HomePage(chatManager);
        JPanel contactsPanel = new ContactsPage(profile, userRegistry);
        JPanel searchPanel = new SearchPage(chatManager.getChatsList(), chatManager);
        JPanel editProfilePanel = new EditProfilePage(profile);
        JPanel savePanel = new SaveLoadPage(profile, userRegistry);

        cardPanel.add(homePanel, "Home");
        cardPanel.add(contactsPanel, "Contacts");
        cardPanel.add(searchPanel, "Search");
        cardPanel.add(editProfilePanel, "Edit Profile");
        cardPanel.add(savePanel, "Save/Load");

        // Register pages with NavigationManager
        NavigationManager.getInstance().registerPage(homePanel, "Home");
        NavigationManager.getInstance().registerPage(contactsPanel, "Contacts");
        NavigationManager.getInstance().registerPage(searchPanel, "Search");
        NavigationManager.getInstance().registerPage(editProfilePanel, "Edit Profile");
        NavigationManager.getInstance().registerPage(savePanel, "Save/Load");

        // Add chat pages dynamically by iterating through the chats in the ChatManager
        for (Chat chat : chatManager.getChatsList()) 
            {
                ChatPage chatPage = new ChatPage(chat, chatManager);
                NavigationManager.getInstance().registerPage(chatPage, "Chat_" + chat.getId());
            }

        dropdown.addActionListener(e -> {
            String selected = (String) dropdown.getSelectedItem();
            if (selected != null) 
                {
                    NavigationManager.getInstance().navigateTo(selected);
                }
        });

        // Container panel that is moved down a bit
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(dropdown, BorderLayout.NORTH);
        topPanel.add(cardPanel, BorderLayout.CENTER);
        containerPanel.add(topPanel, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(title, BorderLayout.NORTH);
        frame.add(containerPanel, BorderLayout.CENTER);
    }

    /**
     * Displays the main menu frame
     */
    public void show() 
    {
        frame.setVisible(true);
    }

    // ### MAIN METHOD TO LAUNCH THE PROGRAM ### // 
    public static void main(String[] args) throws IOException 
    {
        Menu menu = new Menu();
        menu.show();
    }
}
