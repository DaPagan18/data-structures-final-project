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

public class SearchPage extends JPanel 
{
    private final SearchState searchState;
    private final SearchService searchService;
    private final List<Chat> allChats;
    private JPanel resultsList;
    private JTextField searchField;
    private final ChatManager chatManager;

    // ### CONSTRUCTOR ### //
    /*
     * Initializes the search page with the list of all chats and the chat manager.
     *
     * @param allChats The list of all chats to search through.
     * @param chatManager The chat manager for fetching live chat data.
     */
    public SearchPage(List<Chat> allChats, ChatManager chatManager) 
    {
        this.allChats = null; // unused — always fetch live from chatManager
        this.chatManager = chatManager;
        this.searchState = new SearchState();
        this.searchService = new SearchService();

        setLayout(new BorderLayout());

        JPanel searchResultsPanel = createSearchResultsPanel();
        add(searchResultsPanel);
    }

    /*
     * Private method to create the panel for displaying search results.
     *
     * @return The panel containing the search results.
     */
    private JPanel createSearchResultsPanel() 
    {
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

            if (results.isEmpty()) 
                {
                JOptionPane.showMessageDialog(panel, "No messages found containing: \"" + keyword + "\"");
                return;
                }

            searchState.pushSearchState(keyword, results);

            displaySearchResults(results, keyword);
        });

        searchField.addKeyListener(new KeyAdapter() 
        {
            // Allow pressing Enter in the search field to trigger the search button
            @Override
            public void keyPressed(KeyEvent e) 
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) 
                    {
                        searchButton.doClick();
                    }
            }
        });

        panel.add(resultsContainer, BorderLayout.CENTER);

        // Return the panel containing the search results
        return panel;
    }

    /*
     * Private method to display the search results in the UI.
     *
     * @param results The list of search results to display
     * @param keyword The keyword used for the search
     */
    private void displaySearchResults(List<SearchResult> results, String keyword) 
    {
        // Clear previous results
        resultsList.removeAll();

        // If statement to handle no results found
        if (results.isEmpty()) 
            {
                // Display a message indicating no results were found for the given keyword
                JLabel noResults = new JLabel("No results found for: \"" + keyword + "\"");
                noResults.setFont(new Font("Sans Serif", Font.ITALIC, 14));
                noResults.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultsList.add(noResults);
            } else {
                // Iterate through the search results
                for (SearchResult result : results) 
                    {
                        // For each result use the helper method to create a button that shows the chat name, sender and a preview of the message content 
                        JButton resultButton = createResultButton(result);
                        resultsList.add(resultButton);
                        resultsList.add(Box.createVerticalStrut(5));
                    }
            }
        // Refresh the results list to show the new results
        resultsList.revalidate();
        resultsList.repaint();
    }

    /*
     * Private helper method to create an individual button for each search result.
     *
     * @param result The search result for which to create a button
     * @return The created button
     */
    private JButton createResultButton(SearchResult result) 
    {
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
        if (preview.length() > 80) 
            {
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

        // Return the created button for the search result
        return button;
    }
}

/*
 * ### CLASS FOR MANAGING SEARCH STATE AND PERFORMING SEARCHES ###
 * SearchState - Manages the history of search states using a Stack
 *  to allow users to navigate back through previous searches
 */
class SearchState 
{
    // Creating a stack to handle the history of search states
    private final Stack<SearchStateSnapshot> searchHistory;

    // ### CONSTRUCTOR ### //
    /*
     * Initializes the search state stack to be empty when a new SearchState object is created
     */
    public SearchState() 
    {
        this.searchHistory = new Stack<>();
    }

    /*
     * Pushes the current search state onto the history stack
     * 
     * @param keyword The search keyword used for the current search
     * @param results The list of search results obtained from the current search
     */
    public void pushSearchState(String keyword, List<SearchResult> results) 
    {
        searchHistory.push(new SearchStateSnapshot(keyword, new ArrayList<>(results)));
    }

    /*
     * Pops the most recent search state from the history stack
     * 
     * @return The popped search state snapshot, or null if the stack is empty
     */
    public SearchStateSnapshot popSearchState() 
    {
        // If statement to check if the stack is not empty before popping to avoid EmptyStackException
        if (!searchHistory.isEmpty()) 
            {
                return searchHistory.pop();
            }
        return null;
    }

    /*
     * Checks if there is a previous search state available to navigate to
     * 
     * @return boolean True if the stack is not empty, false otherwise
     */
    public boolean canGoBack() 
    {
        return !searchHistory.isEmpty();
    }

    /*
     * Peeks at the current search state without removing it from the history stack
     * 
     * @return The current search state snapshot, or null if the stack is empty
     */
    public SearchStateSnapshot peekCurrentState() 
    {
        if (!searchHistory.isEmpty()) 
            {
                return searchHistory.peek();
            }
        return null;
    }

    /*
     * Gets the number of search states currently stored in the history stack
     * 
     * @return The size of the search history stack
     */
    public int getHistorySize() 
    {
        return searchHistory.size();
    }
}

// ### CLASS FOR REPRESENTING A SNAPSHOT OF THE SEARCH STATE ### //
/*
 * SearchStateSnapshot - A data class to hold the keyword and results of a search state snapshot when pushed onto the history stack
 */
class SearchStateSnapshot 
{
    public String keyword;
    public List<SearchResult> results;

    // ### CONSTRUCTOR ### //
    /*
     * Initializes the search state snapshot with the given keyword and list of search results
     * 
     * @param keyword The search keyword associated with this snapshot
     * @param results The list of search results associated with this snapshot
     */
    public SearchStateSnapshot(String keyword, List<SearchResult> results) 
    {
        this.keyword = keyword;
        this.results = results;
    }
}

// ### CLASS FOR PERFORMING SEARCHES THROUGH CHATS AND MESSAGES ### //
/* 
 * SearchService - Provides functionality to perform searches through a list of chats and their messages based on a given keyword
 */
class SearchService 
{
    // Method to perform a search through the provided list of chats and their messages for the given keyword
    List<SearchResult> search(String keyword, List<Chat> allChats)
     {
        // Create a list to hold the search results
        List<SearchResult> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        // Iterate through each chat 
        for (Chat chat : allChats) 
            {
            // Iterate through each message in the current chat 
            for (Message msg : chat.getMessages()) 
                {
                    // If statement to check if the message content contains the search keyword 
                    if (msg.getMessageContent().toLowerCase().contains(lowerKeyword)) 
                        {
                            // If a match is found, create a new SearchResult object with the current chat and message, and add it to the results list
                            results.add(new SearchResult(chat, msg));
                        }
                }
            }
        // Return the list of search results found for the given keyword
        return results;
    }
}

/// ### CLASS FOR REPRESENTING AN INDIVIDUAL SEARCH RESULT ### //
/*
 * SearchResult - A simple data class to hold a reference to a chat and a message that matched the search criteria
 */
class SearchResult 
{
    Chat chat;
    Message message;

    // ### CONSTRUCTOR ### //
    /*
     * Initializes the search result with the given chat and message that matched the search criteria
     * 
     * @param chat The chat in which the matching message was found
     * @param message The message that matched the search keyword
     */
    public SearchResult(Chat chat, Message message) 
    {
        this.chat = chat;
        this.message = message;
    }
}
