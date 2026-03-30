package datastructProject;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.swing.*;

public class Menu {
    private final JFrame frame;
    private final ChatManager chatManager;
    private CardLayout cards;

    //probably going to need something here that loads the profile from the file
    private final UserRegistry userRegistry = new UserRegistry();
    private Profile profile = new Profile(userRegistry);
    
    //adding a test profile to the user registry to simulate another user being on the system
    private Profile testProfile = new Profile("Calum", "075", "messengerProgram/src/datastructProject/images/calumProfilePic.png", userRegistry);
    private Profile testProfile2 = new Profile("Daniel","123","", userRegistry);
    
    public Menu() throws IOException {

        
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

     
        // dropdown menu options
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

        // Add chat pages dynamically
        for (Chat chat : chatManager.getChatsList()) {
            ChatPage chatPage = new ChatPage(chat, chatManager);
            cardPanel.add(chatPage, "Chat_" + chat.getId());
        }

        dropdown.addActionListener(e -> {
            String selected = (String) dropdown.getSelectedItem();
            if (selected != null) {
                cards.show(cardPanel, selected);
            }
        });

        // container panel that is moved down a bit
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


    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Menu menu = new Menu();
        menu.show();
    }
}
