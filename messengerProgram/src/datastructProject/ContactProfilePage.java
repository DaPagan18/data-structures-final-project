package datastructProject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ContactProfilePage extends JPanel {

    private final JLabel phoneLabel = new JLabel();
    private final JLabel profilePicLabel = new JLabel();
    private final Profile profile;
    private Contact currentContact;

    public ContactProfilePage(Profile profile, Runnable onBack) {
        this.profile = profile;
        setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(profilePicLabel);

        JPanel phoneRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phoneRow.add(phoneLabel);
        centerPanel.add(phoneRow);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void setPage(Contact contact) {
        currentContact = contact;
        phoneLabel.setText("Phone: " + contact.getPhoneNumber());

        String picPath = contact.getProfilePicPath();
        String pathToLoad = (picPath == null || picPath.isEmpty())
                ? "messengerProgram/src/datastructProject/images/profilePicture.png"
                : picPath;
        try {
            BufferedImage img = ImageIO.read(new File(pathToLoad));
            Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            profilePicLabel.setIcon(new ImageIcon(scaled));
        } catch (IOException ex) {
            profilePicLabel.setIcon(null);
            System.out.println("Could not load profile picture: " + pathToLoad);
        }

        revalidate();
        repaint();
    }

  

}
