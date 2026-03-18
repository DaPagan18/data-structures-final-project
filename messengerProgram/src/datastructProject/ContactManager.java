/**
 * Manages the collection of contacts for the messenger program.
 *
 * @author Calum Davies
 */
package datastructProject;

import java.util.HashMap;

public class ContactManager {

    private final HashMap<String, Contact> contactsByNumber = new HashMap<>();

    public void addContact(Contact contact) {
        contactsByNumber.put(contact.getPhoneNumber(), contact);
    }

    public Contact getContact(String phoneNumber) {
        return contactsByNumber.get(phoneNumber);
    }

    public boolean removeContact(String phoneNumber) {
        return contactsByNumber.remove(phoneNumber) != null;
    }

    public boolean hasContact(String phoneNumber) {
        return contactsByNumber.containsKey(phoneNumber);
    }

    public HashMap<String, Contact> getAllContacts() {
        return contactsByNumber;
    }
}
