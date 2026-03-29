/**
 * Profile class that stores users details
 *
 * @author Calum Davies
 */
package datastructProject;

import java.util.HashMap;

public class Profile {

    private String name;
    private String phoneNumber;
    private String profilePicPath;
    private ChatManager chatManager = new ChatManager();
    private final HashMap<String, Contact> contactsByNumber = new HashMap<>();

    public Profile() {
        name = "";
        phoneNumber = "";
        profilePicPath = "";
        
    }

    public Profile(String name, String phoneNumber, String profilePicPath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
 
    }

    //getters and setters for profile data
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    //getters and setters for the hashmap of contacts
    public void addContact(Contact contact) {
        contactsByNumber.put(contact.getPhoneNumber(), contact);
    }

    public Contact getContact(String phoneNumber) {
        return contactsByNumber.get(phoneNumber);
    }

    public boolean removeContact(String phoneNumber) {
        return contactsByNumber.remove(phoneNumber) != null;
    }

    public boolean hasContact(String phoneNumber) {
        return contactsByNumber.containsKey(phoneNumber);
    }

    public HashMap<String, Contact> getAllContacts() {
        return contactsByNumber;
    }

    //getters and setters for the chat manager
    public ChatManager getChatManager() {
        return chatManager;
    }   

    public void setChatManager(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public void addChat(Chat chat) {
        chatManager.addChat(chat);
    }

    public Chat getChat(String chatId) {
        return chatManager.getChat(chatId);
    }

    public boolean deleteChat(String chatId) {
        return chatManager.deleteChat(chatId);
    }

    public HashMap<String, Chat> getAllChats() {
        return chatManager.getAllChats();
    }

    public boolean sendMessage(String chatId, String from, String messageContent) {
        return chatManager.sendMessage(chatId, from, messageContent);
    }

    public boolean deleteMessage(String chatId, String messageId) {
        return chatManager.deleteMessage(chatId, messageId);
    }

}
