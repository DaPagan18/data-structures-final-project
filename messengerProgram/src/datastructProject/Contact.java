/**
 * Contact model for the messenger program.
 *
 * @author Calum Davies
 */
package datastructProject;

public class Contact {

    private String name;
    private String phoneNumber;
    private String profilePicPath;
    private String lastActive;
    private UserRegistry userRegistry;

    public Contact() {
        name = "";
        phoneNumber = "";
        profilePicPath = "";
        lastActive = "";
    }

    public Contact(String phoneNumber, String profilePicPath) {
        
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
        this.name = setName();
        this.lastActive = "";
    }

    /*
    / Another Overloaded constructor used for loading contacts from a save file. 
    */
    public Contact(String name, String phoneNumber, String profilePicPath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
        this.lastActive = "";
    }

    public String getName() {
        return name;
    }

    public String setName(){
        userRegistry = NavigationManager.getInstance().getUserRegistry();
        return (userRegistry.getProfile(this.phoneNumber).getName());
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

    public void setLastActive(String lastActive){
        this.lastActive = lastActive;
    }

    public String getLastActive(){
        return lastActive;
    }

}
