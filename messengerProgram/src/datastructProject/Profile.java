/**
 * Profile class that stores users details
 *
 * @author Calum Davies
 */
package datastructProject;

public class Profile {

    private String name;
    private String phoneNumber;
    private String profilePicPath;

    public Profile() {
        name = "";
        phoneNumber = "";
        profilePicPath = "";
    }

    public Profile(String name, String phoneNumber, String profilePicPath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
 
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

    

}
