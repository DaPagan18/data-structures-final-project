/**
 * Chat display page for viewing and managing conversations.
 *
 * @author Daniel Pagan
 */
package datastructProject;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

public class ChatPage extends JPanel {
    private Chat currentChat;
    private final ChatManager chatManager;
    private final JPanel messagesPanel;
    private JTextArea messageInput;
    private final Runnable onBackHome;
    private JLabel chatTitleLabel;

    public ChatPage(Chat chat, ChatManager chatManager, Runnable onBackHome) {
        this.currentChat = chat;
        this.chatManager = chatManager;
        this.onBackHome = onBackHome;
        
        setLayout(new BorderLayout());
        
        // Header with title and back button
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Chat messages display
        JScrollPane messagesScrollPane = new JScrollPane();
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesScrollPane.setViewportView(messagesPanel);
        add(messagesScrollPane, BorderLayout.CENTER);
        
        // Message input panel
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.SOUTH);
        
        // Load and display chat messages
        loadChatMessages();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(240, 240, 240));
        
        // Back button and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("← Back to Home");
        backButton.addActionListener(e -> onBackHome.run());
        chatTitleLabel = new JLabel(currentChat.getContactName());
        chatTitleLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
        leftPanel.add(backButton);
        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(chatTitleLabel);
        
        // Delete chat button
        JButton deleteChatButton = new JButton("Delete Chat");
        deleteChatButton.setForeground(Color.RED);
        deleteChatButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this entire chat?",
                "Confirm Delete Chat",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                chatManager.deleteChat(currentChat.getId());
                onBackHome.run();
            }
        });
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(deleteChatButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        messageInput = new JTextArea(3, 40);
        messageInput.setLineWrap(true);
        messageInput.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(messageInput);
        
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        return inputPanel;
    }
    
    private void loadChatMessages() {
        messagesPanel.removeAll();
        
        List<Message> messages = currentChat.getMessages();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        for (Message message : messages) {
            JPanel messagePanel = createMessagePanel(message, formatter);
            messagesPanel.add(messagePanel);
            messagesPanel.add(Box.createVerticalStrut(5));
        }
        
        messagesPanel.add(Box.createVerticalGlue());
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }
    
    private JPanel createMessagePanel(Message message, DateTimeFormatter formatter) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        boolean isFromMe = message.getFrom().equals("You");
        
        // Message content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Sender label
        JLabel senderLabel = new JLabel(message.getFrom());
        senderLabel.setFont(new Font("Sans Serif", Font.BOLD, 12));
        contentPanel.add(senderLabel);
        
        // Message text
        JTextArea messageText = new JTextArea(message.getMessageContent());
        messageText.setEditable(false);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setBackground(isFromMe ? new Color(200, 230, 255) : new Color(240, 240, 240));
        messageText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.add(messageText);
        
        // Timestamp
        JLabel timeLabel = new JLabel(message.getTimeSent().format(formatter));
        timeLabel.setFont(new Font("Sans Serif", Font.ITALIC, 10));
        timeLabel.setForeground(Color.GRAY);
        contentPanel.add(timeLabel);
        
        messagePanel.add(contentPanel, BorderLayout.CENTER);
        
        // Action buttons (like and delete)
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        
        // Like button
        JButton likeButton = new JButton(message.isLiked() ? "❤ Liked" : "♡ Like");
        likeButton.setFont(new Font("Sans Serif", Font.PLAIN, 10));
        likeButton.addActionListener(e -> {
            chatManager.likeMessage(currentChat.getId(), message.getId());
            loadChatMessages();
        });
        buttonsPanel.add(likeButton);
        
        // Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Sans Serif", Font.PLAIN, 10));
        deleteButton.setForeground(Color.RED);
        deleteButton.addActionListener(e -> {
            chatManager.deleteMessage(currentChat.getId(), message.getId());
            loadChatMessages();
        });
        buttonsPanel.add(deleteButton);
        
        messagePanel.add(buttonsPanel, BorderLayout.EAST);
        
        return messagePanel;
    }
    
    private void sendMessage() {
        String messageContent = messageInput.getText().trim();
        if (messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message", "Empty Message", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        chatManager.sendMessage(currentChat.getId(), "You", messageContent);
        messageInput.setText("");
        loadChatMessages();
    }
    
    public void updateChat(Chat chat) {
        this.currentChat = chat;
        chatTitleLabel.setText(currentChat.getContactName());
        loadChatMessages();
    }
}
