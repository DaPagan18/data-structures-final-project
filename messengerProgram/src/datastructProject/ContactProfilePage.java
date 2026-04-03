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

public class ContactProfilePage extends JPanel {

    private final JLabel phoneLabel = new JLabel();
    private final JLabel profilePicLabel = new JLabel();
    private final JPanel recentMessagesPanel = new JPanel();
    private final Profile profile;
    private Contact currentContact;
    private ChatManager chatManager;
    private String currentUserPhone;

    public ContactProfilePage(Profile profile, ChatManager chatManager, String currentUserPhone, Runnable onBack) {
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

    public void setPage(Contact contact) {
        currentContact = contact;
        phoneLabel.setText("Phone: " + contact.getPhoneNumber());

        String picPath = contact.getProfilePicPath();
        String pathToLoad;
        if (picPath == null || picPath.isEmpty()) {
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

        recentMessagesPanel.removeAll();

        String currentPhone = NavigationManager.getInstance().getCurrentUserPhone();
        if(!chatManager.checkForChat(currentPhone, currentContact.getPhoneNumber())){
            recentMessagesPanel.setBorder(BorderFactory.createTitledBorder("No messages received from this contact"));
        }
        else{
            recentMessagesPanel.setBorder(BorderFactory.createTitledBorder("Recent messages from this contact:"));
            Chat chat = chatManager.getOrCreateChatForContact(currentPhone, currentContact.getPhoneNumber());
            List<Message> messages = chat.messages.getAll();
            int count = 0;
            int messageIndexCounter = 1;

            while(count != 3 && messageIndexCounter <= messages.size()){
                if(messages.get(messages.size()-messageIndexCounter).getFrom().equals(contact.getPhoneNumber())){
                    recentMessagesPanel.add(new JLabel(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(messages.get(messages.size() - messageIndexCounter).getTimeSent())));
                    recentMessagesPanel.add(new JLabel(messages.get(messages.size() - messageIndexCounter).getMessageContent()));

                    messageIndexCounter++;
                    count++;
                }
                else{
                    messageIndexCounter++;
                }
            }
        }
  

        revalidate();
        repaint();
    }

    private void openChat(){
        Chat chat = chatManager.getOrCreateChatForContact(NavigationManager.getInstance().getCurrentUserPhone(), currentContact.getPhoneNumber());
        ChatPage newChatPage = new ChatPage(chat, chatManager);

        NavigationManager.getInstance().registerPage(newChatPage, "Chat_" + chat.getId());
        NavigationManager.getInstance().navigateTo("Chat_" + chat.getId());
    }

  

}
