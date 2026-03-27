/**
 * Chat model for conversations in the messenger program.
 *
 * @author Daniel Pagan
 */
package datastructProject;

import java.time.LocalDateTime;
import java.util.List;

public class Chat {
    private String id;
    private String contactName;
    private LocalDateTime timeSent;
    private boolean read;
    public SimpleLinkedList messages;

    public Chat() {
        this.id = "";
        this.contactName = "";
        this.timeSent = LocalDateTime.now();
        this.read = false;
        this.messages = new SimpleLinkedList();
    }

    public Chat(String id, String contactName) {
        this.id = id;
        this.contactName = contactName;
        this.timeSent = LocalDateTime.now();
        this.read = false;
        this.messages = new SimpleLinkedList();
    }

    public Chat(String id, String contactName, LocalDateTime timeSent) {
        this.id = id;
        this.contactName = contactName;
        this.timeSent = timeSent;
        this.read = false;
        this.messages = new SimpleLinkedList();
    }

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
        return messages.getAll();
    }

    public void setMessages(List<Message> messages) {
        this.messages = new SimpleLinkedList();
        for (Message msg : messages) {
            this.messages.add(msg);
        }
    }

    public void addMessage(Message message) {
        messages.add(message);
        this.timeSent = LocalDateTime.now();
    }

    public boolean removeMessage(String messageId) {
        return messages.remove(messageId);
    }

    public Message getMessageById(String messageId) {
        return messages.get(messageId);
    }

    public int getMessageCount() {
        return messages.size();
    }

    public void clearAllMessages() {
        messages.clear();
    }

    public Message getLastMessage() {
        return messages.getLast();
    }
}

class LinkedListNode {
    private Message message;
    private LinkedListNode next;
    
    public LinkedListNode(Message message) {
        this.message = message;
        this.next = null;
    }
    
    public Message getMessage() {
        return message;
    }
    
    public void setMessage(Message message) {
        this.message = message;
    }
    
    public LinkedListNode getNext() {
        return next;
    }
    
    public void setNext(LinkedListNode next) {
        this.next = next;
    }
}

class SimpleLinkedList implements Iterable<Message> {
    private LinkedListNode head;
    private int size;
    
    public SimpleLinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    public void add(Message message) {
        LinkedListNode newNode = new LinkedListNode(message);
        
        if (head == null) {
            head = newNode;
        } else {
            LinkedListNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }
    
    public boolean remove(String messageId) {
        if (head == null) {
            return false;
        }
        
        if (head.getMessage().getId().equals(messageId)) {
            head = head.getNext();
            size--;
            return true;
        }
        
        LinkedListNode current = head;
        while (current.getNext() != null) {
            if (current.getNext().getMessage().getId().equals(messageId)) {
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }
        
        return false;
    }
    
    public Message get(String messageId) {
        LinkedListNode current = head;
        while (current != null) {
            if (current.getMessage().getId().equals(messageId)) {
                return current.getMessage();
            }
            current = current.getNext();
        }
        return null;
    }
    
    public List<Message> getAll() {
        List<Message> messages = new java.util.ArrayList<>();
        LinkedListNode current = head;
        while (current != null) {
            messages.add(current.getMessage());
            current = current.getNext();
        }
        return messages;
    }
    
    public List<Message> search(String keyword) {
        List<Message> results = new java.util.ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        LinkedListNode current = head;
        
        while (current != null) {
            Message msg = current.getMessage();
            if (msg.getMessageContent().toLowerCase().contains(lowerKeyword)) {
                results.add(msg);
            }
            current = current.getNext();
        }
        
        return results;
    }
    
    public int size() {
        return size;
    }
    
    public Message getLast() {
        if (head == null) {
            return null;
        }
        LinkedListNode current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        return current.getMessage();
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        head = null;
        size = 0;
    }
    
    @Override
    public java.util.Iterator<Message> iterator() {
        return new LinkedListIterator(head);
    }
    
    private static class LinkedListIterator implements java.util.Iterator<Message> {
        private LinkedListNode current;
        
        public LinkedListIterator(LinkedListNode head) {
            this.current = head;
        }
        
        @Override
        public boolean hasNext() {
            return current != null;
        }
        
        @Override
        public Message next() {
            Message message = current.getMessage();
            current = current.getNext();
            return message;
        }
    }
}