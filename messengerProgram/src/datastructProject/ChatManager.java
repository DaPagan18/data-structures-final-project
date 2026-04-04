/**
 * Manages the collection of chats for the messenger program.
 * Handles chat operations including creating, deleting, and managing messages.
 *
 * @author Daniel Pagan
 */
package datastructProject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ChatManager implements Iterable<Chat> 
{
    // Create a HashMap to store chats
    private final HashMap<String, Chat> chats = new HashMap<>();
    private int messageIdCounter = 0;

    /**
     * Adds a new chat to the chat manager.
     * 
     * @param chat The chat to be added to the manager
     */
    public void addChat(Chat chat) 
    {
        chats.put(chat.getId(), chat);
    }

    /**
     * Retrieves a chat by its ID.
     * 
     * @param chatId The ID of the chat to retrieve
     * @return Chat The chat if found, null otherwise
     */
    public Chat getChat(String chatId) 
    {
        return chats.get(chatId);
    }

    /**
     * Deletes a chat by its ID.
     * 
     * @param chatId The ID of the chat to delete
     * @return boolean True if the chat was successfully deleted, false if the chat was not found
     */
    public boolean deleteChat(String chatId) 
    {
        return chats.remove(chatId) != null;
    }

    /**
     * Retrieves all chats in the chat manager.
     * 
     * @return HashMap<String, Chat> A map of all chats with their IDs as keys
     */
    public HashMap<String, Chat> getAllChats() 
    {
        return chats;
    }

    /**
     * Retrieves a list of all chats in the chat manager.
     * 
     * @return List<Chat> A list of all chats in the manager
     */
    public List<Chat> getChatsList() 
    {
        return new ArrayList<>(chats.values());
    }

    /**
     * Sends a message in a chat.
     * 
     * @param chatId The ID of the chat to send the message in
     * @param from The sender of the message
     * @param messageContent The content of the message to be sent
     * @return boolean True if the message was successfully sent, false if the chat was not found
     */
    public boolean sendMessage(String chatId, String from, String messageContent) 
    {
        Chat chat = chats.get(chatId);
        // Check if chat exists before trying to send a message
        if (chat == null)  
            {
                return false;
            }

        // Generate a unique message ID using the current timestamp and an incrementing counter
        String messageId = "msg_" + System.currentTimeMillis() + "_" + (++messageIdCounter);
        
        // Create a new message object and add it to the chat
        Message message = new Message(messageId, chatId, from, messageContent, LocalDateTime.now());
        chat.addMessage(message);
        
        // Return true to indicate the message was successfully sent
        return true;
    }

    /** 
     * Deletes a message from a chat.
     * 
     * @param chatId The ID of the chat to delete the message from
     * @param messageId The ID of the message to be deleted
     * @return boolean True if the message was successfully deleted, false if the chat or message was not found
     */
    public boolean deleteMessage(String chatId, String messageId) 
    {
        Chat chat = chats.get(chatId);
        if (chat == null) 
            {
                return false;
            }
        return chat.removeMessage(messageId);
    }

    /**
     * Method to like or unlike a message in a chat.
     * 
     * @param chatId The ID of the chat containing the message
     * @param messageId The ID of the message to like or unlike
     * @return boolean True if the message was successfully liked or unliked, false if the chat or message was not found
     */
    public boolean likeMessage(String chatId, String messageId) 
    {
        Chat chat = chats.get(chatId);
        if (chat == null) 
            {
                return false;
            }

        Message message = chat.getMessageById(messageId);
        if (message == null) 
            {
                return false;
            }

        message.setLiked(!message.isLiked());
        return true;
    }

    /**
     * Marks a message as read in a chat.
     * 
     * @param chatId The ID of the chat containing the message
     * @param messageId The ID of the message to mark as read
     * @return boolean True if the message was successfully marked as read, false if the chat or message was not found
     */
    public boolean markMessageAsRead(String chatId, String messageId) 
    {
        Chat chat = chats.get(chatId);
        if (chat == null) 
            {
                return false;
            }

        Message message = chat.getMessageById(messageId);
        if (message == null) 
            {
                return false;
            }

        message.setRead(true);
        return true;
    }

    /**
     * Marks all messages in a chat as read.
     * 
     * @param chatId The ID of the chat containing the messages to mark as read
     */
    public void markAllMessagesAsRead(String chatId)
     {
        Chat chat = chats.get(chatId);
        if (chat != null) 
            {
                // Iterate through all messages in the chat and mark them as read
                for (Message message : chat.getMessages()) 
                    {
                        message.setRead(true);
                    }
            }
    }

    /**
     * Retrieves a list of all unread messages in a chat.
     * 
     * @param chatId The ID of the chat to retrieve unread messages from
     * @return List<Message> A list of all unread messages in a chat
     */
    public List<Message> getUnreadMessages(String chatId) 
    {
        Chat chat = chats.get(chatId);
        if (chat == null) 
            {
                return new ArrayList<>();
            }

        // Create a list of the unread messages
        List<Message> unreadMessages = new ArrayList<>();
        
        // Iterate through all messages in the chat
        for (Message message : chat.getMessages()) 
            {
                if (!message.isRead()) 
                    {
                        // If the message is unread, add it to the list of unread messages
                        unreadMessages.add(message);
                    }
            }
        // Return the list of unread messages
        return unreadMessages;
    }

    /**
     * Searches for messages containing a specific term in a chat.
     * 
     * @param chatId The ID of the chat to search in
     * @param searchTerm The String term to search for
     * @return List<Message> A list of messages that contain the search term
     */
    public List<Message> searchMessagesInChat(String chatId, String searchTerm) 
    {
        Chat chat = chats.get(chatId);
        if (chat == null) 
            {
                return new ArrayList<>();
            }

        // Create a list to store the search results
        List<Message> results = new ArrayList<>();

        // Iterate through all messages in the chat and check if they contain the search term
        for (Message message : chat.getMessages()) 
            {
                if (message.getMessageContent().toLowerCase().contains(searchTerm.toLowerCase())) 
                    {
                        // If the message contains the search term, add it to the results list
                        results.add(message);
                    }
            }
        // Return the list of search results
        return results;
    }

    /**
     * Clears the chat history of a chat.
     * 
     * @param chatId The ID of the chat to clear
     * @return boolean True if the chat history was successfully cleared, false if the chat was not found
     */
    public boolean clearChatHistory(String chatId) 
    {
        Chat chat = chats.get(chatId);
        if (chat == null) 
            {
                return false;
            }
        chat.clearAllMessages();
        return true;
    }

    /**
     * Retrieves the total number of chats in the chat manager.
     * 
     * @return int The size of the chat manager
     */
    public int getChatCount() 
    {
        return chats.size();
    }

    /**
     * Retrieves an existing chat between two contacts or creates a new one if it doesn't exist.
     * 
     * @param phone1 The phone number of the first contact
     * @param phone2 The phone number of the second contact
     * @return Chat The retrieved or created chat
     */
    public Chat getOrCreateChatForContact(String phone1, String phone2)
    {
        // Construct the chat ID 
        String chatId = constructChatId(phone1, phone2);

        // Check if a chat with the constructed ID already exists in the chat manager
        if(chats.containsKey(chatId))
            {
            return chats.get(chatId);
            }else{
                // Otherwise create and return a new chat and add it to the chat manager
                Chat newChat = new Chat(phone1, phone2);
                addChat(newChat);
                return newChat;
            }
    }

    /**
     * Checks if a chat exists between two contacts.
     * 
     * @param phone1 The phone number of the first contact
     * @param phone2 The phone number of the second contact
     * @return boolean True if the chat exists, false otherwise
     */
    public boolean checkForChat(String phone1, String phone2)
    {
        String chatId = constructChatId(phone1, phone2);

        if(chats.containsKey(chatId))
            {
                return true;
            }
            else{
                return false;
            }
    }

    /**
     * Constructs a unique chat ID 
     * 
     * @param phone1 The phone number of the first contact
     * @param phone2 The phone number of the second contact
     * @return chatId The constructed chat ID
     */
    public String constructChatId(String phone1, String phone2)
    {
        // Initialize an empty string to hold the chat ID
        String chatId = "";
        
        // Check if the first phone number is less than or equal to the second phone number
        if(phone1.compareTo(phone2) <= 0)
            {
                // If so, construct the chat ID using the first phone number followed by the second phone number
                chatId = phone1 + "_" + phone2;
            }else 
                {
                    // Else construct the chat ID using the second phone number followed by the first phone number
                    chatId = phone2 + "_" + phone1;
                }
        return chatId;
    }

    /**
     * Returns an iterator over the chats in the chat manager.
     * 
     * @return Iterator<Chat> An iterator over the chats in the chat manager
     */
    @Override
    public Iterator<Chat> iterator() 
    {
        return chats.values().iterator();
    }
}