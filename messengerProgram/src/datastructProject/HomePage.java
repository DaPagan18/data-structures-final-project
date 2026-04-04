/**
 * HomePage class 
 * 
 * @author Stuart Baxter
 */
package datastructProject;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class HomePage extends JPanel 
{
    private final ChatManager chatManager;
    private final JPanel chatsListPanel;

    public HomePage(ChatManager chatManager) 
    {
        this.chatManager = chatManager;
        
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

        // Refresh whenever this panel becomes visible (e.g. navigating back from a chat)
        addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentShown(ComponentEvent e) {
                loadChats();
            }
        });
    }

    /*
     * Private method to load the list of chats into the home page
     */
    private void loadChats() 
    {
        chatsListPanel.removeAll();
        
        // Iterate through the list of chats and create a panel for each one
        for (Chat chat : chatManager.getChatsList())
            {
                JPanel chatPanel = createChatPanel(chat);
                chatsListPanel.add(chatPanel);
                chatsListPanel.add(Box.createVerticalStrut(10));
            }
        
        chatsListPanel.add(Box.createVerticalGlue());
        chatsListPanel.revalidate();
        chatsListPanel.repaint();
    }

    /*
     * Refreshes the list of chats on the home page
     */
    public void refresh() 
    {
        loadChats();
    }

    /*
     * Creates a panel for a specific chat
     */
    private JPanel createChatPanel(Chat chat) 
    {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        chatPanel.setBackground(new Color(245, 245, 245));
        chatPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Left side: chat info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Check for unread messages
        boolean hasUnread = !chatManager.getUnreadMessages(chat.getId()).isEmpty();
        JLabel nameLabel = new JLabel(chat.getContactName() + (hasUnread ? " (Unread)" : ""));
        nameLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
        if (hasUnread) 
            {
                nameLabel.setForeground(new Color(0, 100, 200));
            }
        infoPanel.add(nameLabel);
        
        Message lastMessage = chat.getLastMessage();
        if (lastMessage != null) 
            {
                String preview = lastMessage.getMessageContent().length() > 50 
                    ? lastMessage.getMessageContent().substring(0, 50) + "..." 
                    : lastMessage.getMessageContent();
                JLabel previewLabel = new JLabel(preview);
                previewLabel.setFont(new Font("Sans Serif", Font.PLAIN, 11));
                previewLabel.setForeground(Color.GRAY);
                infoPanel.add(previewLabel);
            
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                JLabel timeLabel = new JLabel(lastMessage.getTimeSent().format(formatter));
                timeLabel.setFont(new Font("Sans Serif", Font.ITALIC, 10));
                timeLabel.setForeground(Color.GRAY);
                infoPanel.add(timeLabel);
            }
        
        // Middle: click area
        JButton openButton = new JButton("Open");
        openButton.addActionListener(e -> NavigationManager.getInstance().navigateTo("Chat_" + chat.getId()));
        
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
            if (confirm == JOptionPane.YES_OPTION) 
                {
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
