/**
 * User registry stores all created user profiles, used for contacts.
 *
 * @author Calum Davies
 */

package datastructProject;

import java.util.HashMap;

public class UserRegistry 
{
    // HashMap to store user profiles, with the phone number as the key for quick lookup
    private final HashMap<String, Profile> profilesByPhoneNumber = new HashMap<>();

    /*
     * Adds a profile to the user registry
     * 
     * @param profile The profile to be added
     */
    public void addProfile(Profile profile)
    {
        profilesByPhoneNumber.put(profile.getPhoneNumber(), profile);
    }

    /*
     * Looks up a profile by phone number
     * 
     * @param phoneNumber The phone number of the profile to look up
     * @return boolean True if the profile exists, false otherwise
     */
    public boolean lookup(String phoneNumber)
    {
        return profilesByPhoneNumber.containsKey(phoneNumber);
    }

    /*
     * Gets a profile by phone number
     * 
     * @param phoneNumber The phone number of the profile to get
     * @return Profile The profile if found, null otherwise
     */
    public Profile getProfile(String phoneNumber)
    {
        return profilesByPhoneNumber.get(phoneNumber);
    }

}
