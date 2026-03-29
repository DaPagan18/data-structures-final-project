/**
 * Profile class that stores users details
 *
 * @author Calum Davies
 */
package datastructProject;

import java.util.HashMap;

public class Profile {

    private String name;
    private String phoneNumber;
    private String profilePicPath;
    private final HashMap<String, Contact> contactsByNumber = new HashMap<>();

    //creates an empty profile ready to be edited
    public Profile(UserRegistry userRegistry) {
        this.name = "";
        this.phoneNumber = generateUniquePhoneNumber(userRegistry);
        this.profilePicPath = "";
        userRegistry.addProfile(this);

    }

  
    //this contsructor is currently used to create test profiles
    public Profile(String name, String profilePicPath, UserRegistry userRegistry){
        this.name = name;
        this.profilePicPath = profilePicPath;
        this.phoneNumber = generateUniquePhoneNumber(userRegistry);
        userRegistry.addProfile(this);
    }


    /*this constructor should be able to be used for loading profile/profiles from a save.
    This is also used to create test profiles used for the demonstration.
     */
    public Profile(String name, String phoneNumber, String profilePicPath, UserRegistry userRegistry) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
        userRegistry.addProfile(this);
    }



    //getters and setters for profile data
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

    //getters and setters for the hashmap of contacts
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

    private String generateUniquePhoneNumber(UserRegistry userRegistry){

        String phoneNumber;

        do {
            StringBuilder randPhoneNum = new StringBuilder(10);
            for (int i = 0; i < 10; i++) {
                int randNum = (int) (Math.random() * 10);
                randPhoneNum.append(randNum);
            }
            phoneNumber = randPhoneNum.toString();
        }while(userRegistry.lookup(phoneNumber));

        return phoneNumber;
    }

    

}
