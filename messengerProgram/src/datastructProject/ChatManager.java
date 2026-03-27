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

    public void addChat(Chat chat) {
        chats.put(chat.getId(), chat);
    }

    public Chat getChat(String chatId) {
        return chats.get(chatId);
    }

    public boolean deleteChat(String chatId) {
        return chats.remove(chatId) != null;
    }

    public HashMap<String, Chat> getAllChats() {
        return chats;
    }

    public List<Chat> getChatsList() {
        return new ArrayList<>(chats.values());
    }

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

    public boolean deleteMessage(String chatId, String messageId) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return false;
        }
        return chat.removeMessage(messageId);
    }

    public boolean likeMessage(String chatId, String messageId) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return false;
        }

        Message message = chat.getMessageById(messageId);
        if (message == null) {
            return false;
        }

        message.setLiked(!message.isLiked());
        return true;
    }

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

    public boolean clearChatHistory(String chatId) {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return false;
        }
        chat.clearAllMessages();
        return true;
    }

    public int getChatCount() {
        return chats.size();
    }

    public boolean chatExists(String chatId) {
        return chats.containsKey(chatId);
    }
}