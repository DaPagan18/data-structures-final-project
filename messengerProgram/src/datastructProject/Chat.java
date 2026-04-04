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
     * Constructor for a
     * 
     * @param timeSent
     * @param participant1PhoneNumber
     * @param participant2PhoneNumber
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

    public String getId() 
    {
        return id;
    }

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getParticipant1PhoneNumber() 
    {
        return participant1PhoneNumber;
    }

    public void setParticipant1PhoneNumber(String participant1PhoneNumber) 
    {
        this.participant1PhoneNumber = participant1PhoneNumber;
    }

    public String getParticipant2PhoneNumber() 
    {
        return participant2PhoneNumber;
    }

    public void setParticipant2PhoneNumber(String participant2PhoneNumber) 
    {
        this.participant2PhoneNumber = participant2PhoneNumber;
    }

    public LocalDateTime getTimeSent() 
    {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) 
    {
        this.timeSent = timeSent;
    }

    public boolean isRead() 
    {
        return read;
    }

    public void setRead(boolean read) 
    {
        this.read = read;
    }

    public List<Message> getMessages() 
    {
        return messages.getAll();
    }

    public void setMessages(List<Message> messages) 
    {
        this.messages = new SimpleLinkedList();
        for (Message msg : messages)
            {
                this.messages.add(msg);
            }
    }

    public void addMessage(Message message) 
    {
        messages.add(message);
        this.timeSent = LocalDateTime.now();
    }

    public boolean removeMessage(String messageId) 
    {
        return messages.remove(messageId);
    }

    public Message getMessageById(String messageId) 
    {
        return messages.get(messageId);
    }

    public int getMessageCount() 
    {
        return messages.size();
    }

    public void clearAllMessages() 
    {
        messages.clear();
    }

    public Message getLastMessage() 
    {
        return messages.getLast();
    }
}

class LinkedListNode 
{
    private Message message;
    private LinkedListNode next;
    
    public LinkedListNode(Message message) 
    {
        this.message = message;
        this.next = null;
    }
    
    public Message getMessage() 
    {
        return message;
    }
    
    public void setMessage(Message message) 
    {
        this.message = message;
    }
    
    public LinkedListNode getNext() 
    {
        return next;
    }
    
    public void setNext(LinkedListNode next) 
    {
        this.next = next;
    }
}

class SimpleLinkedList implements Iterable<Message> 
{
    private LinkedListNode head;
    private int size;
    
    public SimpleLinkedList() 
    {
        this.head = null;
        this.size = 0;
    }
    
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
    
    public int size() 
    {
        return size;
    }
    
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
    
    public boolean isEmpty() 
    {
        return size == 0;
    }
    
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