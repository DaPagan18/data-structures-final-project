package datastructProject;

import java.awt.CardLayout;
import java.util.HashMap;
import javax.swing.JPanel;

public class NavigationManager {
    private static NavigationManager instance;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ChatManager chatManager;
    private UserRegistry userRegistry;
    private String currentUserPhone;
    private HashMap<String, JPanel> pages = new HashMap<>();

    private NavigationManager() {}

    public static NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public void register(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    public void storeChatManager(ChatManager chatManager){
        this.chatManager = chatManager;
    }

    public ChatManager getChatManager(){
        return chatManager;
    }

    public void storeUserRegistry(UserRegistry userRegistry, String currentUserPhone){
        this.userRegistry = userRegistry;
        this.currentUserPhone = currentUserPhone;
    }

    public UserRegistry getUserRegistry(){
        return userRegistry;
    }

    public String getCurrentUserPhone(){
        return currentUserPhone;
    }

    public void navigateTo(String pageName) {
        if (pages.containsKey(pageName) && pages.get(pageName) instanceof ChatPage) {
            String id = pageName.substring(5);
            Chat chat = chatManager.getChat(id);
            if (chat != null) {
                ((ChatPage) pages.get(pageName)).updateChat(chat);
            }
        }
        if (pageName.equals("EditProfile") && pages.containsKey(pageName)) {
            ((EditProfilePage) pages.get(pageName)).refresh();
        }
        if (pageName.equals("Home") && pages.containsKey(pageName)) {
            ((HomePage) pages.get(pageName)).refresh();
        }
        if (pageName.equals("Contacts") && pages.containsKey(pageName)) {
            ((ContactsPage) pages.get(pageName)).refresh();
        }
        cardLayout.show(cardPanel, pageName);
    }

    public void registerPage(JPanel page, String name){
        cardPanel.add(page, name);
        pages.put(name, page);
    }
}
