package datastructProject;

import java.io.FileOutputStream;
import java.io.PrintWriter;
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
             // Iterate through the profile's contacts and write their information to the file
            for (Contact contact : profile.getAllContacts()) {
                printWriter.println("Contact :" + (contactNum));
                printWriter.println(contact.getName());
                printWriter.println(contact.getPhoneNumber());
                printWriter.println(contact.getProfilePicPath());
                contactNum++;
            }

            printWriter.println("[CHATS]");
            printWriter.println(profile.getChatManager().getChatHistory());
        } catch (Exception e) {
                System.out.println("Sorry, there has been a problem opening or writing to the file");
                System.out.println("/t" + e);
         }
         finally
            {
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
                switch (line) {
                    case "[PROFILE]":
                        profile.setName(reader.readLine().split(": ")[1]);
                        profile.setPhoneNumber(reader.readLine().split(": ")[1]);
                        profile.setProfilePicPath(reader.readLine().split(": ")[1]);
                        break;
                    case "[CONTACTS]":
                        profile.getAllContacts().clear(); // Clear existing contacts before loading new ones
                        String name = reader.readLine();
                        String phone = reader.readLine();
                        String pic = reader.readLine();
                        Contact contact = new Contact(name, phone, pic);
                        profile.addContact(contact);
                        break;
                    case "[CHATS]":
                       
                        break;
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
