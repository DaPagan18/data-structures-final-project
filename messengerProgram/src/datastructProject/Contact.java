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
    private HashMap<String, Contact> contactsByUsername;

}
