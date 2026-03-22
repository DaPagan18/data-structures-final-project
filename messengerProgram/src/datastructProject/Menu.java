package datastructProject;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.swing.*;

public class Menu {
    private final JFrame frame;
    private final ChatManager chatManager;
    private CardLayout cards;

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

        // Create ChatManager and sample chats
        this.chatManager = new ChatManager();
        createSampleChats();
        
        // dropdown menu options
        String[] pages = {"Home", "Contacts", "Search", "Edit Profile", "Save/Load"};
        JComboBox<String> dropdown = new JComboBox<>(pages);

        cards = new CardLayout();
        JPanel cardPanel = new JPanel(cards);

        JPanel homePanel = new HomePage(chatManager, () -> {
            cards.show(cardPanel, "Home");
        }, (chat) -> {
            cards.show(cardPanel, "Chat_" + chat.getId());
        });
        JPanel contactsPanel = new ContactsPage();
        JPanel searchPanel = new SearchPage(chatManager.getChatsList(), chatManager);
        JPanel editProfilePanel = new EditProfilePage();
        JPanel savePanel = new SaveLoadPage();

        cardPanel.add(homePanel, "Home");
        cardPanel.add(contactsPanel, "Contacts");
        cardPanel.add(searchPanel, "Search");
        cardPanel.add(editProfilePanel, "Edit Profile");
        cardPanel.add(savePanel, "Save/Load");

        // Add chat pages dynamically
        for (Chat chat : chatManager.getChatsList()) {
            ChatPage chatPage = new ChatPage(chat, chatManager, () -> {
                cards.show(cardPanel, "Home");
            });
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

    private void createSampleChats() {
        // Sample Chat 1
        Chat chat1 = new Chat("chat_001", "Walter White");
        chat1.addMessage(new Message("msg_001", "chat_001", "Walter W", "Hey, how are you doing today?", LocalDateTime.now().minusHours(2)));
        chat1.addMessage(new Message("msg_002", "chat_001", "You", "Pretty good! Just finished a project.", LocalDateTime.now().minusHours(1).minusMinutes(45)));
        chat1.addMessage(new Message("msg_003", "chat_001", "Walter W", "That's awesome! What project were you working on?", LocalDateTime.now().minusHours(1).minusMinutes(30)));
        chat1.addMessage(new Message("msg_004", "chat_001", "You", "Building a search feature for a messenger application", LocalDateTime.now().minusHours(1)));
        chatManager.addChat(chat1);
        
        // Sample Chat 2
        Chat chat2 = new Chat("chat_002", "Jon Snow");
        chat2.addMessage(new Message("msg_005", "chat_002", "Jon S", "Did you see the new Java documentation?", LocalDateTime.now().minusHours(3)));
        chat2.addMessage(new Message("msg_006", "chat_002", "You", "Not yet, is it good?", LocalDateTime.now().minusHours(2).minusMinutes(50)));
        chat2.addMessage(new Message("msg_007", "chat_002", "Jon S", "Yes, they explained Swing components really well", LocalDateTime.now().minusHours(2).minusMinutes(30)));
        chatManager.addChat(chat2);
        
        // Sample Chat 3
        Chat chat3 = new Chat("chat_003", "Tony Soprano");
        chat3.addMessage(new Message("msg_008", "chat_003", "Tony S", "Let's meet up this weekend?", LocalDateTime.now().minusHours(5)));
        chat3.addMessage(new Message("msg_009", "chat_003", "You", "Sure! How about Saturday afternoon?", LocalDateTime.now().minusHours(4).minusMinutes(40)));
        chat3.addMessage(new Message("msg_010", "chat_003", "Tony S", "Perfect! Let's meet at the coffee shop", LocalDateTime.now().minusHours(4)));
        chatManager.addChat(chat3);
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Menu menu = new Menu();
        menu.show();
    }
}
