/**
 * Message model for individual messages in the messenger program.
 *
 * @author Daniel Pagan
 */

package datastructProject;

import java.time.LocalDateTime;

public class Message 
{
    private String id;
    private String chatId;
    private String from;
    private String messageContent;
    private LocalDateTime timeSent;
    private boolean read;
    private boolean liked;

    // ### CONSTRUCTORS ### //
    /**
     * Default constructor for creating a new Message instance
     */
    public Message() 
    {
        this.id = "";
        this.chatId = "";
        this.from = "";
        this.messageContent = "";
        this.timeSent = LocalDateTime.now();
        this.read = false;
        this.liked = false;
    }

    /**
     * Overloaded constructor for creating a new Message instance with specified values
     * 
     * @param id Unique identifier for the message
     * @param chatId Identifier for the chat this message belongs to   
     * @param from Sender of the message
     * @param messageContent Content of the message
     * @param timeSent Timestamp of when the message was sent
    */
    public Message(String id, String chatId, String from, String messageContent, LocalDateTime timeSent) 
    {
        this.id = id;
        this.chatId = chatId;
        this.from = from;
        this.messageContent = messageContent;
        this.timeSent = timeSent;
        this.read = false;
        this.liked = false;
    }

    // ### GETTERS AND SETTERS ### //
    /**
     * Gets the unique identifier for the message
     * 
     * @return String The message ID
     */
    public String getId() 
    {
        return id;
    }

    /**
     * Sets the unique identifier for the message
     * 
     * @param id The message ID
     */
    public void setId(String id) 
    {
        this.id = id;
    }

    /**
     * Gets the identifier for the chat this message belongs to
     * 
     * @return String The chat ID
     */
    public String getChatId() 
    {
        return chatId;
    }

    /**
     * Sets the identifier for the chat this message belongs to
     * 
     * @param chatId The chat ID
     */
    public void setChatId(String chatId) 
    {
        this.chatId = chatId;
    }

    /**
     * Gets the sender of the message
     * 
     * @return String The sender's name or identifier
     */
    public String getFrom() 
    {
        return from;
    }

    /**
     * Sets the sender of the message
     * 
     * @param from The sender's name or identifier
     */
    public void setFrom(String from) 
    {
        this.from = from;
    }

    /**
     * Gets the content of the message
     * 
     * @return String The message content
     */
    public String getMessageContent()
    {
        return messageContent;
    }

    /**
     * Sets the content of the message
     * 
     * @param messageContent The message content
     */
    public void setMessageContent(String messageContent) 
    {
        this.messageContent = messageContent;
    }

    /**
     * Gets the timestamp of when the message was sent
     * 
     * @return LocalDateTime The time the message was sent
     */
    public LocalDateTime getTimeSent() 
    {
        return timeSent;
    }

    /**
     * Sets the timestamp of when the message was sent
     * 
     * @param timeSent The time the message was sent
     */
    public void setTimeSent(LocalDateTime timeSent) 
    {
        this.timeSent = timeSent;
    }

    /**
     * Checks if the message has been read
     * 
     * @return boolean True if the message has been read, false otherwise
     */
    public boolean isRead() 
    {
        return read;
    }

    /**
     * Sets the read status of the message
     * 
     * @param read True if the message has been read, false otherwise
     */
    public void setRead(boolean read) 
    {
        this.read = read;
    }

    /**
     * Checks if the message has been liked
     * 
     * @return boolean True if the message has been liked, false otherwise
     */
    public boolean isLiked() 
    {
        return liked;
    }

    /**
     * Sets the liked status of the message
     * 
     * @param liked True if the message has been liked, false otherwise
     */
    public void setLiked(boolean liked) 
    {
        this.liked = liked;
    }
}
