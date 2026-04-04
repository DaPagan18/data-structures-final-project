/**
 * Profile class that stores users details
 *
 * @author Calum Davies
 */
package datastructProject;

import java.util.HashMap;
import java.util.Iterator;

public class Profile implements Iterable<Contact> 
{
    private String name;
    private String phoneNumber;
    private String profilePicPath;
    private final HashMap<String, Contact> contactsByNumber = new HashMap<>();

    /// ### CONSTRUCTORS ### //
    /**
     * Default constructor for creating a Profile instance
     * Generates a unique phone number and adds it to the user registry.
     * 
     * @param userRegistry The user registry to which the profile will be added
     */
    public Profile(UserRegistry userRegistry) 
    {
        this.name = "";
        this.phoneNumber = generateUniquePhoneNumber(userRegistry);
        this.profilePicPath = "";
        userRegistry.addProfile(this);
    }

    /**
     * Overloaded constructor for creating a Profile instance with specified name and profile picture path
     * Generates a unique phone number and adds it to the user registry.
     * 
     * @param name The name of the profile
     * @param profilePicPath The path to the profile picture
     * @param userRegistry The user registry to which the profile will be added
     */
    public Profile(String name, String profilePicPath, UserRegistry userRegistry)
    {
        this.name = name;
        this.profilePicPath = profilePicPath;
        this.phoneNumber = generateUniquePhoneNumber(userRegistry);
        userRegistry.addProfile(this);
    }


    /** This constructor should be able to be used for loading profile/profiles from a save.
     * This is also used to create test profiles used for the demonstration.
     * 
     * @param name The name of the profile
     * @param phoneNumber The phone number of the profile
     * @param profilePicPath The path to the profile picture
     * @param userRegistry The user registry to which the profile will be added
     */
    public Profile(String name, String phoneNumber, String profilePicPath, UserRegistry userRegistry) 
    {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
        userRegistry.addProfile(this);
    }

    // ### GETTERS AND SETTERS ###
    /**
     * Gets the name of the profile
     * 
     * @return String The name of the profile
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Sets the name of the profile
     * 
     * @param name The name to set
     */
    public void setName(String name) 
    {
        this.name = name;
    }

    /**
     * Gets the phone number of the profile
     * 
     * @return String The phone number of the profile
     */
    public String getPhoneNumber() 
    {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the profile
     * 
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the profile picture path of the profile
     * 
     * @return String The profile picture path of the profile
     */
    public String getProfilePicPath() {
        return profilePicPath;
    }

    /**
     * Sets the profile picture path of the profile
     * 
     * @param profilePicPath The profile picture path to set
     */
    public void setProfilePicPath(String profilePicPath) 
    {
        this.profilePicPath = profilePicPath;
    }

    /**
     * Adds a contact to the profile
     * 
     * @param contact The contact to add
     */
    public void addContact(Contact contact) 
    {
        contactsByNumber.put(contact.getPhoneNumber(), contact);
    }

    /**
     * Gets a contact by phone number
     * 
     * @param phoneNumber The phone number of the contact to get
     * @return Contact The contact if found, null otherwise
     */
    public Contact getContact(String phoneNumber) 
    {
        return contactsByNumber.get(phoneNumber);
    }

    /**
     * Removes a contact from the profile
     * 
     * @param phoneNumber The phone number of the contact to remove
     * @return boolean True if the contact was removed, false otherwise
     */
    public boolean removeContact(String phoneNumber) 
    {
        // Remove the contact from the profile's contact list
        boolean removed = contactsByNumber.remove(phoneNumber) != null;
        
        // If statement to check if the contact was successfully removed
        if (removed) 
            {
                // Get the chat manager instance
                ChatManager chatManager = NavigationManager.getInstance().getChatManager();
                String chatId;
                if (this.phoneNumber.compareTo(phoneNumber) <= 0) 
                    {
                    chatId = this.phoneNumber + "_" + phoneNumber;
                    } else {
                        chatId = phoneNumber + "_" + this.phoneNumber;
                    }
            // Remove the chats associated with the contact
            chatManager.deleteChat(chatId);
            }
        return removed;
    }

    /**
     * Checks if the profile has a contact with the given phone number
     * 
     * @param phoneNumber The phone number of the contact to check
     * @return boolean True if the contact exists, false otherwise
     */
    public boolean hasContact(String phoneNumber) 
    {
        return contactsByNumber.containsKey(phoneNumber);
    }

    /**
     * Gets all contacts of the profile
     * 
     * @return HashMap<String, Contact> The map of all contacts
     */
    public HashMap<String, Contact> getAllContacts() 
    {
        return contactsByNumber;
    }

    /**
     * Generates a unique phone number for the profile
     * 
     * @param userRegistry The user registry to check for existing phone numbers
     * @return String The unique phone number as a string
     */
    private String generateUniquePhoneNumber(UserRegistry userRegistry)
    {
        // Declare a variable to store the generated phone number
        String phoneNumber;

        //Do-while loop to generate a unique phone number that is not already in the user registry
        do {
            // Generate a random 10-digit phone number
            StringBuilder randPhoneNum = new StringBuilder(10);
            for (int i = 0; i < 10; i++) 
                {
                    int randNum = (int) (Math.random() * 10);
                    randPhoneNum.append(randNum);
                }
            phoneNumber = randPhoneNum.toString();
        }while(userRegistry.lookup(phoneNumber));

        return phoneNumber;
    }

    /**
     * Returns an iterator over the contacts in the profile
     * 
     * @return Iterator<Contact> The iterator over the contacts
     */
    @Override
    public Iterator<Contact> iterator() 
    {
        return contactsByNumber.values().iterator();
    }

}
