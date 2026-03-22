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
import java.util.List;

public class ChatManager {
    private final HashMap<String, Chat> chats = new HashMap<>();
    private int messageIdCounter = 0;

    /**
     * Adds a new chat to the manager
     */
    public void addChat(Chat chat) {
        chats.put(chat.getId(), chat);
    }

    /**
     * Retrieves a chat by its ID
     */
    public Chat getChat(String chatId) {
        return chats.get(chatId);
    }

    /**
     * Deletes an entire chat by its ID
     */
    public boolean deleteChat(String chatId) {
        return chats.remove(chatId) != null;
    }

    /**
     * Retrieves all chats
     */
    public HashMap<String, Chat> getAllChats() {
        return chats;
    }

    /**
     * Gets all chats as a list
     */
    public List<Chat> getChatsList() {
        return new ArrayList<>(chats.values());
    }

    /**
     * Sends a message to a specific chat
     */
    public boolean sendMessage(String chatId, String from, String messageContent) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return false;
        }

        String messageId = "msg_" + System.currentTimeMillis() + "_" + (++messageIdCounter);
        Message message = new Message(messageId, chatId, from, messageContent, LocalDateTime.now());
        chat.addMessage(message);
        return true;
    }

    /**
     * Deletes a specific message from a chat
     */
    public boolean deleteMessage(String chatId, String messageId) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return false;
        }
        return chat.removeMessage(messageId);
    }

    /**
     * Likes/unlikes a specific message
     */
    public boolean likeMessage(String chatId, String messageId) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return false;
        }

        Message message = chat.getMessageById(messageId);
        if (message == null) {
            return false;
        }

        // Toggle the liked status
        message.setLiked(!message.isLiked());
        return true;
    }

    /**
     * Marks a message as read
     */
    public boolean markMessageAsRead(String chatId, String messageId) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return false;
        }

        Message message = chat.getMessageById(messageId);
        if (message == null) {
            return false;
        }

        message.setRead(true);
        return true;
    }

    /**
     * Gets all unread messages in a specific chat
     */
    public List<Message> getUnreadMessages(String chatId) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return new ArrayList<>();
        }

        List<Message> unreadMessages = new ArrayList<>();
        for (Message message : chat.getMessages()) {
            if (!message.isRead()) {
                unreadMessages.add(message);
            }
        }
        return unreadMessages;
    }

    /**
     * Searches for messages containing specific content in a chat
     */
    public List<Message> searchMessagesInChat(String chatId, String searchTerm) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return new ArrayList<>();
        }

        List<Message> results = new ArrayList<>();
        for (Message message : chat.getMessages()) {
            if (message.getMessageContent().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(message);
            }
        }
        return results;
    }

    /**
     * Deletes all messages from a specific chat (keeps the chat)
     */
    public boolean clearChatHistory(String chatId) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return false;
        }
        chat.clearAllMessages();
        return true;
    }

    /**
     * Gets the total number of chats
     */
    public int getChatCount() {
        return chats.size();
    }

    /**
     * Checks if a chat exists
     */
    public boolean chatExists(String chatId) {
        return chats.containsKey(chatId);
    }
}
