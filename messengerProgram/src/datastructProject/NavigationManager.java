/**
 * NavigationManager class that manages the navigation between different pages in the application
 *
 * @author Calum Davies
 */
package datastructProject;

import java.awt.CardLayout;
import java.util.HashMap;
import javax.swing.JPanel;

public class NavigationManager 
{
    private static NavigationManager instance;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ChatManager chatManager;
    private UserRegistry userRegistry;
    private String currentUserPhone;
    private HashMap<String, JPanel> pages = new HashMap<>();

    // Private constructor to prevent instantiation from outside the class
    private NavigationManager() {}

    /*
     * Gets the singleton instance of the NavigationManager
     * 
     * @return NavigationManager The instance of the NavigationManager
     */
    public static NavigationManager getInstance() 
    {
        if (instance == null) 
            {
                // Create a new instance of the NavigationManager
                instance = new NavigationManager();
            }
        return instance;
    }

    /*
     * Registers the card layout and card panel for navigation
     * 
     * @param cardLayout The card layout to register
     * @param cardPanel The card panel to register
     */
    public void register(CardLayout cardLayout, JPanel cardPanel) 
    {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    /*
     * Stores the chat manager for use in navigation
     * 
     * @param chatManager The chat manager to store
     */
    public void storeChatManager(ChatManager chatManager)
    {
        this.chatManager = chatManager;
    }

    /*
     * Gets the stored chat manager
     * 
     * @return ChatManager The stored chat manager
     */
    public ChatManager getChatManager()
    {
        return chatManager;
    }

    /*
     * Stores the user registry and current user phone number for use in navigation
     * 
     * @param userRegistry The user registry to store
     * @param currentUserPhone The phone number of the current user
     */
    public void storeUserRegistry(UserRegistry userRegistry, String currentUserPhone)
    {
        this.userRegistry = userRegistry;
        this.currentUserPhone = currentUserPhone;
    }

    /*
     * Gets the stored user registry
     * 
     * @return UserRegistry The stored user registry
     */
    public UserRegistry getUserRegistry()
    {
        return userRegistry;
    }

    /*
     * Gets the phone number of the current user
     * 
     * @return String The phone number of the current user
     */
    public String getCurrentUserPhone()
    {
        return currentUserPhone;
    }

    /*
     * Navigates to the specified page
     * 
     * @param pageName The name of the page to navigate to
     */
    public void navigateTo(String pageName) 
    {
        // If statement to check if the page is a chat page
        if (pages.containsKey(pageName) && pages.get(pageName) instanceof ChatPage) 
            {
                String id = pageName.substring(5);
                Chat chat = chatManager.getChat(id);
                if (chat != null)
                    {
                        // Update the chat page with the latest chat data
                        ((ChatPage) pages.get(pageName)).updateChat(chat);
                    }
            }
            // If statement to check if the page is the edit profile page
            if (pageName.equals("Edit Profile") && pages.containsKey(pageName)) 
                {
                    // Refresh the edit profile page to show the latest profile data
                    ((EditProfilePage) pages.get(pageName)).refresh();
                }
                // If statement to check if the page is the home page 
                if (pageName.equals("Home") && pages.containsKey(pageName)) 
                    {
                        // Refresh the home page to show the latest profile and contact data
                        ((HomePage) pages.get(pageName)).refresh();
                    }
                    // If statement to check if the page is the contacts page
                    if (pageName.equals("Contacts") && pages.containsKey(pageName)) 
                    {
                        // Refresh the contacts page to show the latest contact data
                        ((ContactsPage) pages.get(pageName)).refresh();
                    }
        // Show the page using the card layout
        cardLayout.show(cardPanel, pageName);
    }

    /*
     * Registers a page with the navigation manager
     * 
     * @param page The page to register
     * @param name The name of the page
     */
    public void registerPage(JPanel page, String name)
    {
        cardPanel.add(page, name);
        pages.put(name, page);
    }
}
