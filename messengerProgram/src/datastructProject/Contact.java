/**
 * Contact model for the messenger program.
 *
 * @author Calum Davies
 */
package datastructProject;

public class Contact 
{
    private String name;
    private String phoneNumber;
    private String profilePicPath;
    private String lastActive;
    private UserRegistry userRegistry;

    // ### CONSTRUCTORS ### //
    /**
     * Default constructor for creating a Contact instance.
     */
    public Contact() 
    {
        name = "";
        phoneNumber = "";
        profilePicPath = "";
        lastActive = "";
    }

    /**
     * Overloaded constructor for creating a Contact instance with specified phone number and profile picture path
     * 
     * @param phoneNumber The phone number of the contact
     * @param profilePicPath The path to the contact's profile picture
     */
    public Contact(String phoneNumber, String profilePicPath) 
    {
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
        this.name = setName();
        this.lastActive = "";
    }

    /**
     *  Another Overloaded constructor used for loading contacts from a save file. 
     *
     * @param name The name of the contact
     * @param phoneNumber The phone number of the contact
     * @param profilePicPath The path to the contact's profile picture
    */
    public Contact(String name, String phoneNumber, String profilePicPath) 
    {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicPath = profilePicPath;
        this.lastActive = "";
    }

    // ### GETTERS AND SETTERS ### //
    /**
     * Get the name of the contact
     * 
     * @return name The name of the contact
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the contact
     * by retrieving it from the UserRegistry
     * using the contact's phone number.
     * 
     * @return String The name of the contact
     */
    public String setName()
    {
        userRegistry = NavigationManager.getInstance().getUserRegistry();
        return (userRegistry.getProfile(this.phoneNumber).getName());
    }

    /**
     * Get the phone number of the contact
     * 
     * @return phoneNumber The phone number of the contact
     */
    public String getPhoneNumber() 
    {
        return phoneNumber;
    }

    /**
     * Set the phone number of the contact
     *  
     * @param phoneNumber The phone number to set for the contact
     */
    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get the profile picture path of the contact
     * 
     * @return profilePicPath The path to the contact's profile picture
     */
    public String getProfilePicPath() 
    {
        return profilePicPath;
    }

    /**
     * Set the profile picture path of the contact
     * 
     * @param profilePicPath The path to the contact's profile picture
     */
    public void setProfilePicPath(String profilePicPath) 
    {
        this.profilePicPath = profilePicPath;
    }

    /**
     * Set the last active time of the contact
     * 
     * @param lastActive The time the contact was last active
     */
    public void setLastActive(String lastActive)
    {
        this.lastActive = lastActive;
    }

    /**
     * Get the last active time of the contact
     * 
     * @return lastActive The time the contact was last active
     */
    public String getLastActive()
    {
        return lastActive;
    }
}
