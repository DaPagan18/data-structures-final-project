/*
 * Class to handle the saving and loading of the messenger programme.
 *
 * @author: Calum Sinclair
 */

package datastructProject;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class SaveLoadPage extends JPanel 
{
    // UI components
    private final JLabel title = new JLabel("Save/Load");
    private JPanel buttonBar;

    // Fields of profile and user registry to be able to access the profile and contacts to save and load them
    private Profile profile;
    private UserRegistry userRegistry;
    private Contact contact;
    private Chat chat;

    /*
     * ### CONSTRUCTOR ###
     * Initializes the UI components and sets up the layout
     */
    public SaveLoadPage(Profile profile, UserRegistry userRegistry) 
    {
        this.profile = profile;
        this.userRegistry = userRegistry;

        setLayout(new BorderLayout());

        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JButton saveButton = new JButton("Save Profile");
        saveButton.addActionListener(e -> saveProfile());

        JButton loadButton = new JButton("Load Profile");
        loadButton.addActionListener(e -> chooseProfileFileAndLoad());

        buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonBar.add(saveButton);
        buttonBar.add(loadButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(buttonBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
    }

    /*
     * Method to save the profile and all its data to a text file
     * Creates a new text file with the name "profile_[profile name].txt" 
     * Writes the profile information, iterates and writes the profiles' contacts and chats to the file in a structured format  
     */
    /**
     * 
     */
    private void saveProfile() 
    {
        // Creating a new file output stream and print writer to write to the file
        FileOutputStream outputStream = null;
        PrintWriter printWriter = null;

        // Couunter to differentiate between contacts and chats when writing to the file
        int contactNum = 0;
        int chatNum = 0;

        try {
            outputStream = new FileOutputStream("profile_[ " + profile.getName() + "].txt");
            printWriter = new PrintWriter(outputStream);

            // Write the profile information to the file in a structured format
            printWriter.println("[PROFILE]");
            printWriter.println("Name:" + profile.getName());
            printWriter.println("Phone Number:" + profile.getPhoneNumber());
            printWriter.println("Profile Picture Path:" + profile.getProfilePicPath());

            // Write the contacts information to the file in a structured format
            printWriter.println("[CONTACTS]");
            // Iterate through the profile's contacts and write their information to the file
            for (Contact contact : profile) {
                printWriter.println("Contact :" + (contactNum));
                printWriter.println("Name:" + contact.getName());
                printWriter.println("Phone Number:" + contact.getPhoneNumber());
                printWriter.println("Profile Picture Path:" + contact.getProfilePicPath());
                // Increment the contact number counter for the next contact
                contactNum++;
            }

            // Write the chats information to the file in a structured format
            printWriter.println("[CHATS]");
            // Iterate through the chat manager's chats and write their information to the file
            for (Chat chat : NavigationManager.getInstance().getChatManager()) {
                printWriter.println("Chat:" + (chatNum));
                printWriter.println("ID:" + chat.getId());
                printWriter.println("Participant 1:" + chat.getParticipant1PhoneNumber());
                printWriter.println("Participant 2:" + chat.getParticipant2PhoneNumber());
                printWriter.println("Time Sent:" + chat.getTimeSent());
                printWriter.println("Messages:");
                // Iterate through the chat's messages and write their information to the file
                for (Message message : chat.getMessages()) {
                    printWriter.println("Message ID:" + message.getId());
                    printWriter.println("From:" + message.getFrom());
                    printWriter.println("Content:" + message.getMessageContent());
                    printWriter.println("Time:" + message.getTimeSent());
                    printWriter.println("Read:" + message.isRead());
                    printWriter.println("Liked:" + message.isLiked());
                }
                // Increment the chat number counter for the next chat
                chatNum++;
            }
        } catch (Exception e) 
        {
            // Display an error message if there was a problem saving the file
            JOptionPane.showMessageDialog(this, "Error saving profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Print the stack trace for debugging purposes
            e.printStackTrace();
        } finally 
        {
            if (printWriter != null)
                printWriter.close(); // close the file
        }
        JOptionPane.showMessageDialog(this, "Profile saved successfully!");
    }

    /*
     * Loads a user profile from a file
     * Parses the file line by line to extract the profile information, contacts and chats based on the structured format used when saving the file
     *
     * @param file The file which contains the profile data to be loaded
     */
    private void loadProfileFromFile(File file) 
    {
        // Create a buffered reader to read the file 
        BufferedReader reader = null;
        try {
            // Initialise the buffered reader with the selected file
            reader = new BufferedReader(new FileReader(file));
            String line;
           
            // While loop to read the file line by line and parse the profile, contacts and chats information based on the structured format used when saving the file
            while ((line = reader.readLine()) != null) 
                {
                    if (line.equals("[PROFILE]")) 
                        {
                            // Read the next line after "[PROFILE]" heading 
                            profile.setName(reader.readLine().split(":", 2)[1]);
                            profile.setPhoneNumber(reader.readLine().split(":", 2)[1]);
                            profile.setProfilePicPath(reader.readLine().split(":", 2)[1]);
                            // Add the loaded profile to the user registry
                            userRegistry.addProfile(profile);
                            // Store the user registry in the navigation manager for access by other pages
                            NavigationManager.getInstance().storeUserRegistry(userRegistry, profile.getPhoneNumber());
                        } 
                
                    else if (line.equals("[CONTACTS]")) 
                        {
                            // Clear the profile's current contacts before loading the new ones from the file
                            profile.getAllContacts().clear();
                            // While loop to read the contacts information until the next heading is reached
                            while ((line = reader.readLine()) != null && !line.equals("[CHATS]")) 
                                {
                                    if (line.startsWith("Contact :")) 
                                        {
                                            // Read the contact information and store data in variables
                                            String name = reader.readLine().split(":", 2)[1];
                                            String phone = reader.readLine().split(":", 2)[1];
                                            String pic = reader.readLine().split(":", 2)[1];
                                            // Create a new contact with the loaded information
                                            contact = new Contact(name, phone, pic);
                                            // Add the contact to the profile
                                            profile.addContact(contact);
                                    // Check if the contact's phone number is not already in the user registry
                                    if (!userRegistry.lookup(phone)) 
                                        {
                                            // If not, create a new profile for the contact and add it to the user registry
                                            new Profile(name, phone, pic, userRegistry);
                                        }
                                        }
                                }
                } 
                
                if (line != null && line.equals("[CHATS]")) 
                    {
                        // Clear the chat manager's current chats before loading the new ones from the file
                        NavigationManager.getInstance().getChatManager().getAllChats().clear();
                        line = reader.readLine(); // prime the loop
                        // While loop to read the chats information until the end of the file is reached
                        while (line != null) 
                            {
                                if (line.startsWith("Chat:")) 
                                    {
                                        reader.readLine(); // skip "ID:..." line
                                        String participant1 = reader.readLine().split(":", 2)[1];
                                        String participant2 = reader.readLine().split(":", 2)[1];
                                        String nextLine = reader.readLine();
                                        LocalDateTime chatTimeSent;
                                        // If statement to check if the next line is "Time Sent:" or "Messages:" to correctly parse the data
                                        if (nextLine.startsWith("Time Sent:")) 
                                            {
                                                chatTimeSent = LocalDateTime.parse(nextLine.split(":", 2)[1]);
                                                reader.readLine(); // Skip "Messages:" line
                                            } else 
                                            {
                                                    chatTimeSent = LocalDateTime.now();
                                                    // nextLine is "Messages:", already read, no skip
                                            }
                                        // Create a new chat with the loaded information 
                                        chat = new Chat(chatTimeSent, participant1, participant2);
                                        // Add the chat to the chat manager
                                        NavigationManager.getInstance().getChatManager().addChat(chat);
                                        line = reader.readLine(); // prime the message loop
                                        while (line != null && !line.startsWith("Chat:")) 
                                            {
                                                if (line.startsWith("Message ID:")) 
                                                    {
                                                        // Read the message information and store data in variables
                                                        String messageId = line.split(":", 2)[1];
                                                        String from = reader.readLine().split(":", 2)[1];
                                                        String content = reader.readLine().split(":", 2)[1];
                                                        LocalDateTime time = LocalDateTime.parse(reader.readLine().split(":", 2)[1]);
                                                        boolean read = Boolean.parseBoolean(reader.readLine().split(":", 2)[1]);
                                                        boolean liked = Boolean.parseBoolean(reader.readLine().split(":", 2)[1]);
                                                        // Create a new message with the loaded information and add it to the chat
                                                        Message message = new Message(messageId, chat.getId(), from, content, time);
                                                        message.setRead(read);
                                                        message.setLiked(liked);
                                                        chat.addMessage(message);
                                                    }
                                                line = reader.readLine();
                                            }
                                } else {
                                    line = reader.readLine();
                                }
                            }
                    }
                }
            // Display a success message after loading the profile and its data successfully
            JOptionPane.showMessageDialog(this, "Profile loaded successfully!");
            ChatManager cm = NavigationManager.getInstance().getChatManager();
            // Iterate through the chat manager's chats
            for (Chat c : cm) 
                {
                    // For each chat create a new chat page and register it with the navigation manager
                    ChatPage chatPage = new ChatPage(c, cm);
                    // Use the chat's ID to create a unique page name for each chat page when registering with the navigation manager
                    NavigationManager.getInstance().registerPage(chatPage, "Chat_" + c.getId());
                }
            // After loading the profile and its data, navigate back to the home page
            NavigationManager.getInstance().navigateTo("Home");
        }catch (FileNotFoundException e)
		{
            // Display an error message if the selected file was not found
		    JOptionPane.showMessageDialog(this, "File not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} 
        catch (Exception e) 
        {
            // Display an error message if there was a problem loading the file
            JOptionPane.showMessageDialog(this, "Error loading profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally 
        {
            try {
                if (reader != null) {
                    reader.close(); // close the file
                }
            } catch (IOException e) 
            {
                // Display an error message if there was a problem closing the file
                JOptionPane.showMessageDialog(this, "Error closing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     * Helper method to open a file chooser and allow the user to select a profile file to load
     * It then creates a new File object for the selected file
     */
    private void chooseProfileFileAndLoad() 
    {
        // Open a file chooser dialog to selcet the profile file to load
        JFileChooser fileChooser = new JFileChooser();

        // Set the current directory to the user's home directory and filter for .txt files
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Profile Files", "txt"));
        int result = fileChooser.showOpenDialog(this);

        // If statement to check if the user selected a file and clicked "Open"
        if (result == JFileChooser.APPROVE_OPTION) 
        {
            // Get the selected file and call the method to load the profile from that file
            File selectedFile = fileChooser.getSelectedFile();
            loadProfileFromFile(selectedFile);
        }
    }

}
