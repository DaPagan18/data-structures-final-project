/**
 * Message model for individual messages in the messenger program.
 *
 * @author Daniel Pagan
 */

package datastructProject;

import java.time.LocalDateTime;

public class Message {
    private String id;
    private String chatId;
    private String from;
    private String messageContent;
    private LocalDateTime timeSent;
    private boolean read;
    private boolean liked;

    public Message() {
        this.id = "";
        this.chatId = "";
        this.from = "";
        this.messageContent = "";
        this.timeSent = LocalDateTime.now();
        this.read = false;
        this.liked = false;
    }

    public Message(String id, String chatId, String from, String messageContent, LocalDateTime timeSent) {
        this.id = id;
        this.chatId = chatId;
        this.from = from;
        this.messageContent = messageContent;
        this.timeSent = timeSent;
        this.read = false;
        this.liked = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
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

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

}
