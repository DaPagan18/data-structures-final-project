/**
 * ContactProfilePage class represents the profile page of a contact in the messenger application. 
 * It displays the contact's phone number, profile picture, and recent messages received from that contact.
 * 
 * @author Calum Davies
 */
package datastructProject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ContactProfilePage extends JPanel 
{
    private final JLabel phoneLabel = new JLabel();
    private final JLabel profilePicLabel = new JLabel();
    private final JPanel recentMessagesPanel = new JPanel();
    private final Profile profile;
    private Contact currentContact;
    private ChatManager chatManager;
    private String currentUserPhone;

    // ### CONSTRUCTOR ### //
    /**
     * constructor for creating a ContactProfilePage instance.
     *
     * @param profile The profile of the user.
     * @param chatManager The chat manager for handling chat operations.
     * @param currentUserPhone The phone number of the current user.
     * @param onBack The action to perform when navigating back.
     */
    public ContactProfilePage(Profile profile, ChatManager chatManager, String currentUserPhone, Runnable onBack) 
    {
        this.profile = profile;
        this.chatManager = chatManager;
        this.currentUserPhone = currentUserPhone;

        setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());

        JButton messageButton = new JButton("Open Chat");
        messageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageButton.addActionListener(e -> openChat());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        topPanel.add(messageButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(profilePicLabel);

        JPanel phoneRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phoneRow.add(phoneLabel);
        centerPanel.add(phoneRow);

        recentMessagesPanel.setLayout(new BoxLayout(recentMessagesPanel, BoxLayout.Y_AXIS));

        JPanel recentMessagesWrapper = new JPanel(new BorderLayout());
        recentMessagesWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        recentMessagesWrapper.add(recentMessagesPanel, BorderLayout.CENTER);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(recentMessagesWrapper);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Sets the contact information on the profile page, including phone number,
     * profile picture, and recent messages received from that contact.
     *
     * @param contact The contact whose information should be displayed.
     */
    public void setPage(Contact contact) 
    {
        currentContact = contact;
        phoneLabel.setText("Phone: " + contact.getPhoneNumber());

        String picPath = contact.getProfilePicPath();
        String pathToLoad;

        // If the contact doesn't have a profile picture, load the default one
        if (picPath == null || picPath.isEmpty()) 
            {
            pathToLoad = "messengerProgram/src/datastructProject/images/profilePicture.png";
        } else {
            pathToLoad = picPath;
        }
        
        try {
            BufferedImage img = ImageIO.read(new File(pathToLoad));
            Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            profilePicLabel.setIcon(new ImageIcon(scaled));
        } catch (IOException ex) {
            profilePicLabel.setIcon(null);
            System.out.println("Could not load profile picture: " + pathToLoad);
        }

        // Clear previous messages
        recentMessagesPanel.removeAll();

        // Check for messages from this contact and display the 3 most recent ones
        String currentPhone = NavigationManager.getInstance().getCurrentUserPhone();
        
        if(!chatManager.checkForChat(currentPhone, currentContact.getPhoneNumber()))
            {
            recentMessagesPanel.setBorder(BorderFactory.createTitledBorder("No messages received from this contact"));
        }
        else{
            recentMessagesPanel.setBorder(BorderFactory.createTitledBorder("Recent messages from this contact:"));
            Chat chat = chatManager.getOrCreateChatForContact(currentPhone, currentContact.getPhoneNumber());
            
            // Get all messages in the chat and filter for those sent by the contact, displaying the 3 most recent ones
            List<Message> messages = chat.messages.getAll();
            int count = 0;
            int messageIndexCounter = 1;

            while(count != 3 && messageIndexCounter <= messages.size())
                {
                    // Check if the message was sent by the contact, if so display it, otherwise skip to the next message
                    if(messages.get(messages.size()-messageIndexCounter).getFrom().equals(contact.getPhoneNumber()))
                        {
                            recentMessagesPanel.add(new JLabel(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(messages.get(messages.size() - messageIndexCounter).getTimeSent())));
                            recentMessagesPanel.add(new JLabel(messages.get(messages.size() - messageIndexCounter).getMessageContent()));

                            // Increment the message index counter and the count of messages displayed
                            messageIndexCounter++;
                            count++;
                        }
                    else{
                        // Else increment the message index counter to check the next most recent message
                        messageIndexCounter++;
                        }
                }
        }
        // Refresh the panel to show the updated information
        revalidate();
        repaint();
    }

    /**
     * Private helper method to open the chat page for the current contact.
     * Method retrieves or creates a chat for the contact and navigates to the corresponding chat page.
     */
    private void openChat()
    {
        Chat chat = chatManager.getOrCreateChatForContact(NavigationManager.getInstance().getCurrentUserPhone(), currentContact.getPhoneNumber());
        ChatPage newChatPage = new ChatPage(chat, chatManager);

        NavigationManager.getInstance().registerPage(newChatPage, "Chat_" + chat.getId());
        NavigationManager.getInstance().navigateTo("Chat_" + chat.getId());
    }  
}
