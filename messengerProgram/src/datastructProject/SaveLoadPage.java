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

public class SaveLoadPage extends JPanel {

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
     * Writes the profile information, contacts, and chats to the file in a structured format  
     */
    private void saveProfile() 
    {
        // Creating a new file output stream and print writer to write to the file
        FileOutputStream outputStream = null;
        PrintWriter printWriter = null;

        // Couunter to differentiate between contacts and chats when writing to the file
        int contactNum = 0;

        try {
            outputStream = new FileOutputStream("profile_[ " + profile.getName() + "].txt");
            printWriter = new PrintWriter(outputStream);

            printWriter.println("[PROFILE]");
            printWriter.println("Name:" + profile.getName());
            printWriter.println("Phone Number:" + profile.getPhoneNumber());
            printWriter.println("Profile Picture Path:" + profile.getProfilePicPath());

            printWriter.println("[CONTACTS]");
            // Iterate through the profile's contacts and write their information to the file
            for (Contact contact : profile) {
                printWriter.println("Contact :" + (contactNum));
                printWriter.println("Name:" + contact.getName());
                printWriter.println("Phone Number:" + contact.getPhoneNumber());
                printWriter.println("Profile Picture Path:" + contact.getProfilePicPath());
                contactNum++;
            }

            printWriter.println("[CHATS]");
            int chatNum = 0;
            for (Chat chat : NavigationManager.getInstance().getChatManager()) {
                printWriter.println("Chat:" + (chatNum));
                printWriter.println("ID:" + chat.getId());
                printWriter.println("Participant 1:" + chat.getParticipant1PhoneNumber());
                printWriter.println("Participant 2:" + chat.getParticipant2PhoneNumber());
                printWriter.println("Time Sent:" + chat.getTimeSent());
                printWriter.println("Messages:");
                for (Message message : chat.getMessages()) {
                    printWriter.println("Message ID:" + message.getId());
                    printWriter.println("From:" + message.getFrom());
                    printWriter.println("Content:" + message.getMessageContent());
                    printWriter.println("Time:" + message.getTimeSent());
                    printWriter.println("Read:" + message.isRead());
                    printWriter.println("Liked:" + message.isLiked());
                }
                chatNum++;
            }
        } catch (Exception e) 
        {
            System.out.println("Sorry, there has been a problem opening or writing to the file");
            System.out.println("/t" + e);
        } finally 
        {
            if (printWriter != null)
                printWriter.close(); // close the file
        }
        JOptionPane.showMessageDialog(this, "Profile saved successfully!");
    }

    private void loadProfileFromFile(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("[PROFILE]")) {
                    profile.setName(reader.readLine().split(": ")[1]);
                    profile.setPhoneNumber(reader.readLine().split(": ")[1]);
                    profile.setProfilePicPath(reader.readLine().split(": ")[1]);
                } 
                
                else if (line.equals("[CONTACTS]")) {
                    profile.getAllContacts().clear();
                    while ((line = reader.readLine()) != null && !line.equals("[CHATS]")) {
                        if (line.startsWith("Contact :")) {
                            String name = reader.readLine();
                            String phone = reader.readLine();
                            String pic = reader.readLine();
                            contact = new Contact(name, phone, pic);
                            profile.addContact(contact);
                        }
                    }
                } 
                
                else if (line.equals("[CHATS]")) {
                    NavigationManager.getInstance().getChatManager().getAllChats().clear();
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Chat :")) {
                            String participant1 = reader.readLine().split(": ")[1];
                            String participant2 = reader.readLine().split(": ")[1];
                            String nextLine = reader.readLine();
                            LocalDateTime chatTimeSent;
                            if (nextLine.startsWith("Time Sent:")) {
                                chatTimeSent = LocalDateTime.parse(nextLine.split(": ")[1]);
                                reader.readLine(); // Skip "Messages:" line
                            } else {
                                chatTimeSent = LocalDateTime.now();
                                // nextLine is "Messages:", already read, no skip
                            }
                            chat = new Chat(chatTimeSent, participant1, participant2);
                            NavigationManager.getInstance().getChatManager().addChat(chat);
                            while ((line = reader.readLine()) != null && !line.startsWith("Chat :")) {
                                if (line.startsWith("Message ID: ")) {
                                    String messageId = line.split(": ")[1];
                                    String from = reader.readLine().split(": ")[1];
                                    String content = reader.readLine().split(": ")[1];
                                    LocalDateTime time = LocalDateTime.parse(reader.readLine().split(": ")[1]);
                                    boolean read = Boolean.parseBoolean(reader.readLine().split(": ")[1]);
                                    boolean liked = Boolean.parseBoolean(reader.readLine().split(": ")[1]);

                                    Message message = new Message(messageId, chat.getId(), from, content, time);
                                    message.setRead(read);
                                    message.setLiked(liked);
                                    chat.addMessage(message);
                                }
                            }
                            if (line == null) {
                                break; // End of file reached
                            }
                        }
                    }
                    System.out.println("loading profile from file.");
                }
            }
            JOptionPane.showMessageDialog(this, "Profile loaded successfully!");
        }catch (FileNotFoundException e)
		{
		    JOptionPane.showMessageDialog(this, "File not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally 
        {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) 
            {
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
