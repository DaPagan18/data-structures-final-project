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

    public Contact() {
        name = "";
        phoneNumber = "";
        profilePicPath = "";
        lastActive = "";
    }

    public Contact(String name, String phoneNumber, String profilePicPath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
        this.lastActive = "";
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

    public void setLastActive(String lastActive){
        this.lastActive = lastActive;
    }

    public String getLastActive(){
        return lastActive;
    }

}
