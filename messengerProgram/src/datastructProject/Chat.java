/**
 * Chat model for conversations in the messenger program.
 *
 * @author Daniel Pagan
 */
package datastructProject;

import java.time.LocalDateTime;
import java.util.List;

public class Chat 
{
    private String id;
    private String participant1PhoneNumber;
    private String participant2PhoneNumber;
    private LocalDateTime timeSent;
    private boolean read;
    public SimpleLinkedList messages;

    // ### CONSTRUCTORS ### //
    /**
     * Default constructor for a Chat.
     * Initializes all fields to default values.
     */
    public Chat() 
    {
        this.id = "";
        this.participant1PhoneNumber = "";
        this.participant2PhoneNumber = "";
        this.timeSent = LocalDateTime.now();
        this.read = false;
        this.messages = new SimpleLinkedList();
    }

    /**
     * Constructor for a Chat with specified participant phone numbers.
     * 
     * @param participant1PhoneNumber The phone number of the first participant
     * @param participant2PhoneNumber The phone number of the second participant 
     */
    public Chat(String participant1PhoneNumber, String participant2PhoneNumber) 
    {
        this.id = generateId(participant1PhoneNumber, participant2PhoneNumber);
        this.participant1PhoneNumber = participant1PhoneNumber;
        this.participant2PhoneNumber = participant2PhoneNumber;
        this.timeSent = LocalDateTime.now();
        this.read = false;
        this.messages = new SimpleLinkedList();
    }

    /**
     * Constructor for a Chat with a specified timestamp and participant phone numbers.
     * Used when loading a chat from a save file.
     *
     * @param timeSent The timestamp of the last message sent
     * @param participant1PhoneNumber The phone number of the first participant
     * @param participant2PhoneNumber The phone number of the second participant
     */
    public Chat(LocalDateTime timeSent, String participant1PhoneNumber, String participant2PhoneNumber) 
    {
        this.id = generateId(participant1PhoneNumber, participant2PhoneNumber);
        this.participant1PhoneNumber = participant1PhoneNumber;
        this.participant2PhoneNumber = participant2PhoneNumber;
        this.timeSent = timeSent;
        this.read = false;
        this.messages = new SimpleLinkedList();
    }

    /**
     * Generates a chat ID by using the users and recipients phone number.
     * This ensures the same two participants always produce the same chat ID regardless of order.
     *
     * @param phone1 The phone number of the first participant
     * @param phone2 The phone number of the second participant
     * @return String The generated chat ID
     */
    private static String generateId(String phone1, String phone2)
    {
        if(phone1.compareTo(phone2) <= 0)
            {
                return phone1 + "_" + phone2;
            } else 
                {
                    return phone2 + "_" + phone1;
                }
    }

    /**
     * Gets the name of the other participant in this chat relative to the current user.
     *
     * @return String The name of the contact in this chat
     */
    public String getContactName()
    {
        UserRegistry userRegistry = NavigationManager.getInstance().getUserRegistry();
        String currentPhone = NavigationManager.getInstance().getCurrentUserPhone();
        
        if(!currentPhone.equals(this.participant1PhoneNumber))
            {
            return userRegistry.getProfile(this.participant1PhoneNumber).getName();
            }else
                {
                    return userRegistry.getProfile(this.participant2PhoneNumber).getName();
            }
    }

    /**
     * Gets the unique ID of this chat.
     *
     * @return String The chat ID
     */
    public String getId() 
    {
        return id;
    }

    /**
     * Sets the unique ID of this chat.
     *
     * @param id The chat ID to set
     */
    public void setId(String id) 
    {
        this.id = id;
    }

    /**
     * Gets the phone number of the first participant.
     *
     * @return String The first participant's phone number
     */
    public String getParticipant1PhoneNumber() 
    {
        return participant1PhoneNumber;
    }

    /**
     * Sets the phone number of the first participant.
     *
     * @param participant1PhoneNumber The phone number to set
     */
    public void setParticipant1PhoneNumber(String participant1PhoneNumber) 
    {
        this.participant1PhoneNumber = participant1PhoneNumber;
    }

    /**
     * Gets the phone number of the second participant.
     *
     * @return String The second participant's phone number
     */
    public String getParticipant2PhoneNumber() 
    {
        return participant2PhoneNumber;
    }

    /**
     * Sets the phone number of the second participant.
     *
     * @param participant2PhoneNumber The phone number to set
     */
    public void setParticipant2PhoneNumber(String participant2PhoneNumber) 
    {
        this.participant2PhoneNumber = participant2PhoneNumber;
    }

    /**
     * Gets the timestamp of the last message sent in this chat.
     *
     * @return LocalDateTime The time the last message was sent
     */
    public LocalDateTime getTimeSent() 
    {
        return timeSent;
    }

    /**
     * Sets the timestamp of the last message sent in this chat.
     *
     * @param timeSent The timestamp to set
     */
    public void setTimeSent(LocalDateTime timeSent) 
    {
        this.timeSent = timeSent;
    }

    /**
     * Returns whether this chat has been read by the current user.
     *
     * @return boolean True if the chat has been read, false otherwise
     */
    public boolean isRead() 
    {
        return read;
    }

    /**
     * Sets the read status of this chat.
     *
     * @param read True to mark as read, false to mark as unread
     */
    public void setRead(boolean read) 
    {
        this.read = read;
    }

    /**
     * Gets all messages in this chat as a list.
     *
     * @return List<Message> All messages in the chat
     */
    public List<Message> getMessages() 
    {
        return messages.getAll();
    }

    /**
     * Replaces all messages in this chat with the provided list.
     *
     * @param messages The list of messages to set
     */
    public void setMessages(List<Message> messages) 
    {
        this.messages = new SimpleLinkedList();
        for (Message msg : messages)
            {
                this.messages.add(msg);
            }
    }

    /**
     * Adds a message to the end of this chat and updates the last sent timestamp.
     *
     * @param message The message to add
     */
    public void addMessage(Message message) 
    {
        messages.add(message);
        this.timeSent = LocalDateTime.now();
    }

    /**
     * Removes a message from this chat by its ID.
     *
     * @param messageId The ID of the message to remove
     * @return boolean True if the message was found and removed, false otherwise
     */
    public boolean removeMessage(String messageId) 
    {
        return messages.remove(messageId);
    }

    /**
     * Retrieves a message from this chat by its ID.
     *
     * @param messageId The ID of the message to retrieve
     * @return Message The message if found, null otherwise
     */
    public Message getMessageById(String messageId) 
    {
        return messages.get(messageId);
    }

    /**
     * Gets the total number of messages in this chat.
     *
     * @return int The number of messages
     */
    public int getMessageCount() 
    {
        return messages.size();
    }

    /**
     * Removes all messages from this chat.
     */
    public void clearAllMessages() 
    {
        messages.clear();
    }

    /**
     * Gets the most recently added message in this chat.
     *
     * @return Message The last message, or null if the chat is empty
     */
    public Message getLastMessage() 
    {
        return messages.getLast();
    }
}

/**
 * A node in the singly-linked list used to store messages in a Chat.
 * Each node holds a Message and a reference to the next node.
 */
class LinkedListNode 
{
    private Message message;
    private LinkedListNode next;
    
    /**
     * Creates a new node wrapping the given message.
     *
     * @param message The message to store in this node
     */
    public LinkedListNode(Message message) 
    {
        this.message = message;
        this.next = null;
    }
    
    /**
     * Gets the message stored in this node.
     *
     * @return Message The stored message
     */
    public Message getMessage() 
    {
        return message;
    }
    
    /**
     * Sets the message stored in this node.
     *
     * @param message The message to store
     */
    public void setMessage(Message message) 
    {
        this.message = message;
    }
    
    /**
     * Gets the next node in the linked list.
     *
     * @return LinkedListNode The next node, or null if this is the last node
     */
    public LinkedListNode getNext() 
    {
        return next;
    }
    
    /**
     * Sets the next node in the linked list.
     *
     * @param next The node to link to
     */
    public void setNext(LinkedListNode next) 
    {
        this.next = next;
    }
}

/**
 * A custom singly-linked list for storing Message objects in a Chat.
 * Supports append, remove by ID, get by ID, keyword search, and iteration.
 */
class SimpleLinkedList implements Iterable<Message> 
{
    private LinkedListNode head;
    private int size;
    
    /**
     * Creates an empty linked list.
     */
    public SimpleLinkedList() 
    {
        this.head = null;
        this.size = 0;
    }
    
    /**
     * Appends a message to the end of the list.
     *
     * @param message The message to add
     */
    public void add(Message message) 
    {
        LinkedListNode newNode = new LinkedListNode(message);
        
        if (head == null) 
            {
            head = newNode;
            } else 
                {
                    LinkedListNode current = head;
                    while (current.getNext() != null) 
                        {
                            current = current.getNext();
                        }
            current.setNext(newNode);
            }
        size++;
    }
    
    /**
     * Removes the message with the given ID from the list.
     *
     * @param messageId The ID of the message to remove
     * @return boolean True if the message was found and removed, false otherwise
     */
    public boolean remove(String messageId) 
    {
        if (head == null) 
            {
                return false;
            }
        
        if (head.getMessage().getId().equals(messageId)) 
            {
                head = head.getNext();
                size--;
                return true;
            }
        
        LinkedListNode current = head;
        while (current.getNext() != null) 
            {
                if (current.getNext().getMessage().getId().equals(messageId)) 
                    {
                        current.setNext(current.getNext().getNext());
                        size--;
                        return true;
                    }
                current = current.getNext();
            }
        
        return false;
    }
    
    /**
     * Retrieves the message with the given ID.
     *
     * @param messageId The ID of the message to find
     * @return Message The message if found, null otherwise
     */
    public Message get(String messageId) 
    {
        LinkedListNode current = head;
        while (current != null) 
            {
                if (current.getMessage().getId().equals(messageId)) 
                    {
                        return current.getMessage();
                    }
                current = current.getNext();
            }
        return null;
    }
    
    /**
     * Returns all messages in the list as an ArrayList.
     *
     * @return List<Message> All messages in insertion order
     */
    public List<Message> getAll() 
    {
        List<Message> messages = new java.util.ArrayList<>();
        LinkedListNode current = head;
        while (current != null) 
            {
                messages.add(current.getMessage());
                current = current.getNext();
            }
        return messages;
    }
    
    /**
     * Searches for messages whose content contains the given keyword (case-insensitive).
     *
     * @param keyword The keyword to search for
     * @return List<Message> All messages containing the keyword
     */
    public List<Message> search(String keyword) 
    {
        List<Message> results = new java.util.ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        LinkedListNode current = head;
        
        while (current != null) 
            {
            Message msg = current.getMessage();
            if (msg.getMessageContent().toLowerCase().contains(lowerKeyword)) 
                {
                    results.add(msg);
                }
            current = current.getNext();
            }
    
        return results;
    }
    
    /**
     * Returns the number of messages in the list.
     *
     * @return int The size of the list
     */
    public int size() 
    {
        return size;
    }
    
    /**
     * Returns the last message in the list without removing it.
     *
     * @return Message The last message, or null if the list is empty
     */
    public Message getLast() 
    {
        if (head == null) 
            {
                return null;
            }
        LinkedListNode current = head;
        while (current.getNext() != null) 
            {
                current = current.getNext();
            }
        return current.getMessage();
    }
    
    /**
     * Returns whether the list contains no messages.
     *
     * @return boolean True if empty, false otherwise
     */
    public boolean isEmpty() 
    {
        return size == 0;
    }
    
    /**
     * Removes all messages from the list.
     */
    public void clear() 
    {
        head = null;
        size = 0;
    }
    
    @Override
    public java.util.Iterator<Message> iterator() 
    {
        return new LinkedListIterator(head);
    }
    
    private static class LinkedListIterator implements java.util.Iterator<Message> 
    {
        private LinkedListNode current;
        
        public LinkedListIterator(LinkedListNode head)
        {
            this.current = head;
        }
        
        @Override
        public boolean hasNext() 
        {
            return current != null;
        }
        
        @Override
        public Message next() 
        {
            Message message = current.getMessage();
            current = current.getNext();
            return message;
        }
    }
}