package datastructProject;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Menu {
    private JFrame frame;

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

        // Create sample chats data
        List<Chat> allChats = createSampleChats();
        
        // dropdown menu options
        String[] pages = {"Home", "Contacts", "Search", "New Chat", "Edit Profile", "Save/Load"};
        JComboBox<String> dropdown = new JComboBox<>(pages);

        JPanel cards = new JPanel(new CardLayout());

        JPanel homePanel = new HomePage();
        JPanel contactsPanel = new ContactsPage();
        JPanel searchPanel = new SearchPage(allChats);
        JPanel newChatPanel = new NewChatPage();
        JPanel editProfilePanel = new EditProfilePage();
        JPanel savePanel = new SaveLoadPage();

        cards.add(homePanel, "Home");
        cards.add(contactsPanel, "Contacts");
        cards.add(searchPanel, "Search");
        cards.add(newChatPanel, "New Chat");
        cards.add(editProfilePanel, "Edit Profile");
        cards.add(savePanel, "Save/Load");

        dropdown.addActionListener(e -> {
            CardLayout c1 = (CardLayout)(cards.getLayout());
            c1.show(cards, (String) dropdown.getSelectedItem());
        });

        // container panel that is moved down a bit
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(dropdown, BorderLayout.NORTH);
        topPanel.add(cards, BorderLayout.CENTER);
        containerPanel.add(topPanel, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(title, BorderLayout.NORTH);
        frame.add(containerPanel, BorderLayout.CENTER);
    }

    private List<Chat> createSampleChats() {
        List<Chat> chats = new ArrayList<>();
        
        // Sample Chat 1
        Chat chat1 = new Chat("chat_001", "Walter White");
        chat1.messages.add(new Message("msg_001", "chat_001", "Walter W", "Hey, how are you doing today?", LocalDateTime.now().minusHours(2)));
        chat1.messages.add(new Message("msg_002", "chat_001", "You", "Pretty good! Just finished a project.", LocalDateTime.now().minusHours(1).minusMinutes(45)));
        chat1.messages.add(new Message("msg_003", "chat_001", "Walter W", "That's awesome! What project were you working on?", LocalDateTime.now().minusHours(1).minusMinutes(30)));
        chat1.messages.add(new Message("msg_004", "chat_001", "You", "Building a search feature for a messenger application", LocalDateTime.now().minusHours(1)));
        chats.add(chat1);
        
        // Sample Chat 2
        Chat chat2 = new Chat("chat_002", "Jon Snow");
        chat2.messages.add(new Message("msg_005", "chat_002", "Jon S", "Did you see the new Java documentation?", LocalDateTime.now().minusHours(3)));
        chat2.messages.add(new Message("msg_006", "chat_002", "You", "Not yet, is it good?", LocalDateTime.now().minusHours(2).minusMinutes(50)));
        chat2.messages.add(new Message("msg_007", "chat_002", "Jon S", "Yes, they explained Swing components really well", LocalDateTime.now().minusHours(2).minusMinutes(30)));
        chats.add(chat2);
        
        // Sample Chat 3
        Chat chat3 = new Chat("chat_003", "Tony Soprano");
        chat3.messages.add(new Message("msg_008", "chat_003", "Tony S", "Let's meet up this weekend?", LocalDateTime.now().minusHours(5)));
        chat3.messages.add(new Message("msg_009", "chat_003", "You", "Sure! How about Saturday afternoon?", LocalDateTime.now().minusHours(4).minusMinutes(40)));
        chat3.messages.add(new Message("msg_010", "chat_003", "Tony S", "Perfect! Let's meet at the coffee shop", LocalDateTime.now().minusHours(4)));
        chats.add(chat3);
        
        return chats;
    }

    public void show() {
        frame.setVisible(true);
    }
    public static void main(String[] args) throws IOException {
        Menu menu = new Menu();
        menu.show();
    }
}
