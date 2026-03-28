/**
 * User registry stores all created user profiles, used for contacts.
 *
 * @author Calum Davies
 */

package datastructProject;

import java.util.HashMap;

public class UserRegistry {

    private final HashMap<String, Profile> profilesByPhoneNumber = new HashMap<>();

    public void addProfile(Profile profile){
        profilesByPhoneNumber.put(profile.getPhoneNumber(), profile);
    }

    public boolean lookup(String phoneNumber){
        return profilesByPhoneNumber.containsKey(phoneNumber);
    }

}
