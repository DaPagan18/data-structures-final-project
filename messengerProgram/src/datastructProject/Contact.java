/**
 * Contact model for the messenger program.
 *
 * @author Calum Davies
 */
package datastructProject;

import java.util.HashMap;

public class Contact {
    
    private String name;
    private String phoneNumber;
    private String profilePicPath;
    private HashMap<String, Contact> contactsByNumber;

    public Contact(){
        name = "";
        phoneNumber = "";
        profilePicPath = "";
        contactsByNumber = new HashMap<>();
    }

    public Contact(String name, String phoneNumber, String profilePicPath){
        this.name = name;
        this.profilePicPath = profilePicPath;
        this.profilePicPath = profilePicPath;
        contactsByNumber = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public Contact getContactsByUsername(String name) {
        return contactsByNumber.get(name);
    }

    public void addContact(Contact contact){
        contactsByNumber.put(contact.getPhoneNumber(), contact);
    }

    

}
