package datastructProject;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SaveLoadPage extends JPanel {

    private final JLabel title = new JLabel("Save/Load");
    private JPanel buttonBar;

    private Profile profile;
    private UserRegistry userRegistry;
    private Contact contact;
    private Chat chat;

    public SaveLoadPage(Profile profile, UserRegistry userRegistry) {
        this.profile = profile;
        this.userRegistry = userRegistry;

        setLayout(new BorderLayout());

        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JButton saveButton = new JButton("Save Profile");
        saveButton.addActionListener(e -> saveProfile());

        JButton loadButton = new JButton("Load Profile");
        loadButton.addActionListener(e -> loadProfile());

        buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonBar.add(saveButton);
        buttonBar.add(loadButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(buttonBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
    }

    private void saveProfile() {
        FileOutputStream outputStream = null;
        PrintWriter printWriter = null;
        int contactNum = 0;

        try {
            outputStream = new FileOutputStream("profile_[ " + profile.getName() + "].txt");
            printWriter = new PrintWriter(outputStream);

            printWriter.println("[PROFILE]");
            printWriter.println("Name: " + profile.getName());
            printWriter.println("Phone Number: " + profile.getPhoneNumber());
            printWriter.println("Profile Picture Path: " + profile.getProfilePicPath());

            printWriter.println("[CONTACTS]");
            // Iterate through the profile's contacts and write their information to the
            // file
            for (Contact contact : profile) {
                printWriter.println("Contact :" + (contactNum));
                printWriter.println(contact.getName());
                printWriter.println(contact.getPhoneNumber());
                printWriter.println(contact.getProfilePicPath());
                contactNum++;
            }

            printWriter.println("[CHATS]");
            int chatNum = 0;
            for (Chat chat : profile.getChatManager()) {
                printWriter.println("Chat :" + (chatNum));
                printWriter.println("ID: " + chat.getId());
                printWriter.println("Participant 1: " + chat.getParticipant1PhoneNumber());
                printWriter.println("Participant 2: " + chat.getParticipant2PhoneNumber());
                printWriter.println("Time Sent: " + chat.getTimeSent());
                printWriter.println("Messages:");
                for (Message message : chat.getMessages()) {
                    printWriter.println("  Message ID: " + message.getId());
                    printWriter.println("  From: " + message.getFrom());
                    printWriter.println("  Content: " + message.getMessageContent());
                    printWriter.println("  Time: " + message.getTimeSent());
                    printWriter.println("  Read: " + message.isRead());
                    printWriter.println("  Liked: " + message.isLiked());
                }
                chatNum++;
            }
        } catch (Exception e) {
            System.out.println("Sorry, there has been a problem opening or writing to the file");
            System.out.println("/t" + e);
        } finally {
            if (printWriter != null)
                printWriter.close(); // close the file
        }
        JOptionPane.showMessageDialog(this, "Profile saved successfully!");
    }

    private void loadProfile() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("profile_[ " + profile.getName() + "].txt"));
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
                    profile.getChatManager().getAllChats().clear();
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Chat :")) {
                            String participant1 = reader.readLine().split(": ")[1];
                            String participant2 = reader.readLine().split(": ")[1];
                            LocalDateTime timeSent = LocalDateTime.parse(reader.readLine().split(": ")[1]);
                            chat = new Chat(timeSent, participant1, participant2);
                            profile.getChatManager().addChat(chat);

                            reader.readLine(); // Skip "Messages:" line
                            while ((line = reader.readLine()) != null && !line.startsWith("Chat :")) {
                                if (line.startsWith("Message ID: ")) {
                                    String messageId = line.split(": ")[1];
                                    String from = reader.readLine().split(": ")[1];
                                    String content = reader.readLine().split(": ")[1];
                                    LocalDateTime time = LocalDateTime.parse(reader.readLine().split(": ")[1]);
                                    boolean read = Boolean.parseBoolean(reader.readLine().split(": ")[1]);
                                    boolean liked = Boolean.parseBoolean(reader.readLine().split(": ")[1]);

                                    Message message = new Message(messageId, chat.getId(), from, content, timeSent);
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
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, your file was not found.");
        } catch (IOException e) {
            System.out.println("Sorry, there has been a problem reading the file.");
        } finally {
            try {
                if (reader != null) {
                    reader.close(); // close the file
                }
            } catch (IOException e) {
                System.out.println("Sorry, there has been a problem closing the file.");
            }
        }
        JOptionPane.showMessageDialog(this, "Profile loaded successfully!");
    }
}
