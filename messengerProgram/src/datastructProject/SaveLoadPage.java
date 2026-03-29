package datastructProject;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.*;

public class SaveLoadPage extends JPanel {
    public SaveLoadPage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(20));
        
        JLabel title = new JLabel("Save/Load");
        title.setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 24));
        add(title);

    }

    public static void saveProfile(Profile profile) {
        
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
            for (Contact contact : profile.getContacts()) {
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
    }


    public static void loadProfileData(Profile profile) {
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
                        profile.getContacts().clear(); // Clear existing contacts before loading new ones
                        break;
                    case "[CHATS]":
                        
                        break;
                    default:
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
    }
}
