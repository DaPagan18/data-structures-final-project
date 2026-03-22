/**
 * Handles search functionality for finding messages within chats.
 * Implements a search state management system using a Stack to allow users to navigate back through previous search results.
 *
 * @author Daniel Pagan
 */

package datastructProject;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.*;

public class SearchPage extends JPanel {
    private CardLayout cardLayout;
    private SearchState searchState;
    private SearchService searchService;
    private List<Chat> allChats;
    private JPanel resultsList;
    private JTextField searchField;
    private ChatManager chatManager;
    
    public SearchPage(List<Chat> allChats, ChatManager chatManager) {
        this.allChats = allChats;
        this.chatManager = chatManager;
        this.cardLayout = new CardLayout();
        this.searchState = new SearchState();
        this.searchService = new SearchService();
        
        setLayout(cardLayout);
        
        // Create search results panel
        JPanel searchResultsPanel = createSearchResultsPanel();
        add(searchResultsPanel, "SearchResults");
        
        // Show search results by default
        cardLayout.show(this, "SearchResults");
    }
    
    private JPanel createSearchResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Title
        JLabel title = new JLabel("Search");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);
        
        // Search input panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        
        searchPanel.add(new JLabel("Keyword: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Results panel with scroll
        JPanel resultsContainer = new JPanel(new BorderLayout());
        resultsList = new JPanel();
        resultsList.setLayout(new BoxLayout(resultsList, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(resultsList);
        resultsContainer.add(scrollPane, BorderLayout.CENTER);
        
        // Search button action - uses Stack to save state
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter a keyword to search");
                return;
            }
            
            // Run search through messages
            List<SearchResult> results = searchService.search(keyword, allChats);
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "No messages found containing: \"" + keyword + "\"");
                return;
            }
            
            // Push current state onto Stack before updating
            searchState.pushSearchState(keyword, results);
            
            // Display results
            displaySearchResults(results, keyword);
        });
        
        // Allow search on Enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }
        });
        
        panel.add(resultsContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void displaySearchResults(List<SearchResult> results, String keyword) {
        // Clear previous results
        resultsList.removeAll();
        
        if (results.isEmpty()) {
            JLabel noResults = new JLabel("No results found for: \"" + keyword + "\"");
            noResults.setFont(new Font("Sans Serif", Font.ITALIC, 14));
            noResults.setAlignmentX(Component.LEFT_ALIGNMENT);
            resultsList.add(noResults);
        } else {
            // Display results
            for (SearchResult result : results) {
                JButton resultButton = createResultButton(result);
                resultsList.add(resultButton);
                resultsList.add(Box.createVerticalStrut(5));
            }
        }
        
        resultsList.revalidate();
        resultsList.repaint();
    }
    
    private JButton createResultButton(SearchResult result) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        // Result info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Chat name and sender
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(240, 240, 240));
        JLabel chatName = new JLabel("Chat: " + result.chat.getContactName());
        chatName.setFont(new Font("Sans Serif", Font.BOLD, 12));
        JLabel sender = new JLabel(" | From: " + result.message.getFrom());
        sender.setFont(new Font("Sans Serif", Font.PLAIN, 11));
        sender.setForeground(new Color(100, 100, 100));
        topPanel.add(chatName);
        topPanel.add(sender);
        
        // Message content preview
        String preview = result.message.getMessageContent();
        if (preview.length() > 80) {
            preview = preview.substring(0, 80) + "...";
        }
        JLabel messagePreview = new JLabel(preview);
        messagePreview.setFont(new Font("Sans Serif", Font.PLAIN, 11));
        messagePreview.setForeground(new Color(50, 50, 50));
        messagePreview.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        
        infoPanel.add(topPanel, BorderLayout.NORTH);
        infoPanel.add(messagePreview, BorderLayout.CENTER);
        
        button.add(infoPanel, BorderLayout.CENTER);
        
        // Action: open chat when clicked
        button.addActionListener(e -> {
            openChatScreen(result.chat);
        });
        
        return button;
    }
    
    private void openChatScreen(Chat chat) {
        // Remove previous chat screen if exists
        for (Component comp : getComponents()) {
            if (comp instanceof ChatScreen) {
                remove(comp);
            }
        }
        
        // Create and add new chat screen
        ChatScreen chatScreen = new ChatScreen(chat, chatManager, () -> {
            // Back button callback: show search results again
            cardLayout.show(SearchPage.this, "SearchResults");
        });
        add(chatScreen, "ChatDetail");
        
        // Show chat screen
        cardLayout.show(this, "ChatDetail");
    }
}

class SearchState {
    private Stack<SearchStateSnapshot> searchHistory;
    
    public SearchState() {
        this.searchHistory = new Stack<>();
    }
    
    public void pushSearchState(String keyword, List<SearchResult> results) {
        searchHistory.push(new SearchStateSnapshot(keyword, new ArrayList<>(results)));
    }
    
    public SearchStateSnapshot popSearchState() {
        if (!searchHistory.isEmpty()) {
            return searchHistory.pop();
        }
        return null;
    }
    
    public boolean canGoBack() {
        return !searchHistory.isEmpty();
    }
    
    public SearchStateSnapshot peekCurrentState() {
        if (!searchHistory.isEmpty()) {
            return searchHistory.peek();
        }
        return null;
    }
    
    public int getHistorySize() {
        return searchHistory.size();
    }
}

class SearchStateSnapshot {
    public String keyword;
    public List<SearchResult> results;
    
    public SearchStateSnapshot(String keyword, List<SearchResult> results) {
        this.keyword = keyword;
        this.results = results;
    }
}

class SearchService {
    List<SearchResult> search(String keyword, List<Chat> allChats) {
        List<SearchResult> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Chat chat : allChats) {
            for (Message msg : chat.getMessages()) {
                if (msg.getMessageContent().toLowerCase().contains(lowerKeyword)) {
                    results.add(new SearchResult(chat, msg));
                }
            }
        }
        return results;
    }
}

class SearchResult {
    Chat chat;
    Message message;
    
    public SearchResult(Chat chat, Message message) {
        this.chat = chat;
        this.message = message;
    }
}

class ChatScreen extends JPanel {
    private Chat chat;
    private ChatManager chatManager;
    private Runnable onBack;
    
    public ChatScreen(Chat chat, ChatManager chatManager, Runnable onBack) {
        this.chat = chat;
        this.chatManager = chatManager;
        this.onBack = onBack;
        
        setLayout(new BorderLayout());
        
        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("← Back");
        backButton.addActionListener(e -> onBack.run());
        
        JLabel chatTitle = new JLabel(chat.getContactName());
        chatTitle.setFont(new Font("Sans Serif", Font.BOLD, 18));
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(chatTitle, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Messages panel
        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        
        for (Message msg : chat.getMessages()) {
            JPanel messageItem = new JPanel();
            messageItem.setLayout(new BorderLayout());
            messageItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            messageItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            messageItem.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            // Left side: message content
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            
            JLabel sender = new JLabel(msg.getFrom());
            sender.setFont(new Font("Sans Serif", Font.BOLD, 12));
            contentPanel.add(sender);
            
            JLabel content = new JLabel(msg.getMessageContent());
            content.setFont(new Font("Sans Serif", Font.PLAIN, 11));
            contentPanel.add(content);
            
            JLabel timestamp = new JLabel(msg.getTimeSent().toString());
            timestamp.setFont(new Font("Sans Serif", Font.ITALIC, 10));
            timestamp.setForeground(Color.GRAY);
            contentPanel.add(timestamp);
            
            messageItem.add(contentPanel, BorderLayout.CENTER);
            
            // Right side: action buttons
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
            
            JButton likeButton = new JButton(msg.isLiked() ? "❤ Liked" : "♡ Like");
            likeButton.setFont(new Font("Sans Serif", Font.PLAIN, 10));
            likeButton.addActionListener(e -> {
                chatManager.likeMessage(chat.getId(), msg.getId());
                // Refresh: go back and reopen chat to show updated state
                onBack.run();
            });
            buttonsPanel.add(likeButton);
            
            JButton deleteButton = new JButton("Delete");
            deleteButton.setFont(new Font("Sans Serif", Font.PLAIN, 10));
            deleteButton.setForeground(Color.RED);
            deleteButton.addActionListener(e -> {
                chatManager.deleteMessage(chat.getId(), msg.getId());
                // Refresh: go back and reopen chat to show updated state
                onBack.run();
            });
            buttonsPanel.add(deleteButton);
            
            messageItem.add(buttonsPanel, BorderLayout.EAST);
            messageItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            
            messagesPanel.add(messageItem);
        }
        
        JScrollPane scrollPane = new JScrollPane(messagesPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
}
