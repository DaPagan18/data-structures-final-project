package datastructProject;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class HomePage extends JPanel {
    private final ChatManager chatManager;
    private Runnable onBackHome;
    private final ChatOpenListener onChatOpen;
    private final JPanel chatsListPanel;

    @FunctionalInterface
    public interface ChatOpenListener {
        void onChatOpened(Chat chat);
    }

    public HomePage(ChatManager chatManager, Runnable onBackHome, ChatOpenListener onChatOpen) {
        this.chatManager = chatManager;
        this.onBackHome = onBackHome;
        this.onChatOpen = onChatOpen;
        
        setLayout(new BorderLayout());
        
        // Title
        JLabel title = new JLabel("Chats");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        
        // Chats list panel
        chatsListPanel = new JPanel();
        chatsListPanel.setLayout(new BoxLayout(chatsListPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(chatsListPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load chats
        loadChats();
    }

    private void loadChats() {
        chatsListPanel.removeAll();
        
        for (Chat chat : chatManager.getChatsList()) {
            JPanel chatPanel = createChatPanel(chat);
            chatsListPanel.add(chatPanel);
            chatsListPanel.add(Box.createVerticalStrut(10));
        }
        
        chatsListPanel.add(Box.createVerticalGlue());
        chatsListPanel.revalidate();
        chatsListPanel.repaint();
    }

    private JPanel createChatPanel(Chat chat) {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        chatPanel.setBackground(new Color(245, 245, 245));
        chatPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Left side: chat info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel nameLabel = new JLabel(chat.getContactName());
        nameLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
        infoPanel.add(nameLabel);
        
        Message lastMessage = chat.getLastMessage();
        if (lastMessage != null) {
            String preview = lastMessage.getMessageContent().length() > 50 
                ? lastMessage.getMessageContent().substring(0, 50) + "..." 
                : lastMessage.getMessageContent();
            JLabel previewLabel = new JLabel(preview);
            previewLabel.setFont(new Font("Sans Serif", Font.PLAIN, 11));
            previewLabel.setForeground(Color.GRAY);
            infoPanel.add(previewLabel);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            JLabel timeLabel = new JLabel(lastMessage.getTimeSent().format(formatter));
            timeLabel.setFont(new Font("Sans Serif", Font.ITALIC, 10));
            timeLabel.setForeground(Color.GRAY);
            infoPanel.add(timeLabel);
        }
        
        // Middle: click area
        JButton openButton = new JButton("Open");
        openButton.addActionListener(e -> onChatOpen.onChatOpened(chat));
        
        // Right side: delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setForeground(Color.RED);
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete chat with " + chat.getContactName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                chatManager.deleteChat(chat.getId());
                loadChats();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(openButton);
        buttonPanel.add(deleteButton);
        
        chatPanel.add(infoPanel, BorderLayout.CENTER);
        chatPanel.add(buttonPanel, BorderLayout.EAST);
        
        return chatPanel;
    }
}
