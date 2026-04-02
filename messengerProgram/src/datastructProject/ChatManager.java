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

public class ChatManager implements Iterable<Chat> {
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
        message.setRead(true); // Sent messages are read by default
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

    public void markAllMessagesAsRead(String chatId) {
        Chat chat = chats.get(chatId);
        if (chat != null) {
            for (Message message : chat.getMessages()) {
                message.setRead(true);
            }
        }
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


    public Chat getOrCreateChatForContact(String phone1, String phone2){
        String chatId = constructChatId(phone1, phone2);


        if(chats.containsKey(chatId)){
            return chats.get(chatId);
        }else{
            Chat newChat = new Chat(phone1, phone2);
            addChat(newChat);
            return newChat;
        }

    }

    public boolean checkForChat(String phone1, String phone2){
        String chatId = constructChatId(phone1, phone2);

        if(chats.containsKey(chatId)){
            return true;
        }
        else{
            return false;
        }

    }

    public String constructChatId(String phone1, String phone2){

        String chatId = "";
        if(phone1.compareTo(phone2) <= 0){
            chatId = phone1 + "_" + phone2;
        }else {
            chatId = phone2 + "_" + phone1;
        }
        return chatId;
    }

    @Override
    public Iterator<Chat> iterator() {
        return chats.values().iterator();
    }
}