/**
 * Chat model for conversations in the messenger program.
 *
 * @author Daniel Pagan
 */
package datastructProject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String id;
    private String contactName;
    private LocalDateTime timeSent;
    private boolean read;
    public List<Message> messages;

    public Chat() {
        this.id = "";
        this.contactName = "";
        this.timeSent = LocalDateTime.now();
        this.read = false;
        this.messages = new ArrayList<>();
    }

    public Chat(String id, String contactName) {
        this.id = id;
        this.contactName = contactName;
        this.timeSent = LocalDateTime.now();
        this.read = false;
        this.messages = new ArrayList<>();
    }

    public Chat(String id, String contactName, LocalDateTime timeSent) {
        this.id = id;
        this.contactName = contactName;
        this.timeSent = timeSent;
        this.read = false;
        this.messages = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * Adds a message to the chat
     */
    public void addMessage(Message message) {
        messages.add(message);
        this.timeSent = LocalDateTime.now();
    }

    /**
     * Removes a specific message from the chat by message ID
     */
    public boolean removeMessage(String messageId) {
        return messages.removeIf(msg -> msg.getId().equals(messageId));
    }

    /**
     * Gets a message by its ID
     */
    public Message getMessageById(String messageId) {
        return messages.stream()
                .filter(msg -> msg.getId().equals(messageId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the total number of messages in the chat
     */
    public int getMessageCount() {
        return messages.size();
    }

    /**
     * Clears all messages from the chat
     */
    public void clearAllMessages() {
        messages.clear();
    }

    /**
     * Gets the most recent message
     */
    public Message getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }
}
