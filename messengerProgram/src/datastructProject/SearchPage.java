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
    private final SearchState searchState;
    private final SearchService searchService;
    private final List<Chat> allChats;
    private JPanel resultsList;
    private JTextField searchField;
    private final ChatManager chatManager;

    public SearchPage(List<Chat> allChats, ChatManager chatManager) {
        this.allChats = null; // unused — always fetch live from chatManager
        this.chatManager = chatManager;
        this.searchState = new SearchState();
        this.searchService = new SearchService();

        setLayout(new BorderLayout());

        JPanel searchResultsPanel = createSearchResultsPanel();
        add(searchResultsPanel);
    }

    private JPanel createSearchResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Search");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Keyword: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel resultsContainer = new JPanel(new BorderLayout());
        resultsList = new JPanel();
        resultsList.setLayout(new BoxLayout(resultsList, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(resultsList);
        resultsContainer.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter a keyword to search");
                return;
            }

            List<SearchResult> results = searchService.search(keyword, chatManager.getChatsList());

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "No messages found containing: \"" + keyword + "\"");
                return;
            }

            searchState.pushSearchState(keyword, results);

            displaySearchResults(results, keyword);
        });

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
        resultsList.removeAll();

        if (results.isEmpty()) {
            JLabel noResults = new JLabel("No results found for: \"" + keyword + "\"");
            noResults.setFont(new Font("Sans Serif", Font.ITALIC, 14));
            noResults.setAlignmentX(Component.LEFT_ALIGNMENT);
            resultsList.add(noResults);
        } else {
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

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(240, 240, 240));
        JLabel chatName = new JLabel("Chat: " + result.chat.getContactName());
        chatName.setFont(new Font("Sans Serif", Font.BOLD, 12));
        JLabel sender = new JLabel(" | From: " + result.message.getFrom());
        sender.setFont(new Font("Sans Serif", Font.PLAIN, 11));
        sender.setForeground(new Color(100, 100, 100));
        topPanel.add(chatName);
        topPanel.add(sender);

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

        button.addActionListener(e -> NavigationManager.getInstance().navigateTo("Chat_" + result.chat.getId()));

        return button;
    }
}

class SearchState {
    private final Stack<SearchStateSnapshot> searchHistory;

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
