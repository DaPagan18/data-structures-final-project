/**
 * Edit profile page to change details
 * 
 * @author Stuart Baxter
 */

package datastructProject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EditProfilePage extends JPanel 
{
    Font font = new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 18);
  
    Profile profile;
    String usernameText;
    String sPhoneNumberText;
    String profilePicPath;
    
    JLabel profileImageLabel = new JLabel();
    private JLabel usernameValueLabel;
    private JLabel phoneValueLabel;

    // ### CONSTRUCTOR ### //
    /**
     * Overloaded constructor for creating an EditProfilePage instance.
     *
     * @param profile The profile of the user.
     */
    public EditProfilePage(Profile profile) 
    {
        this.profile = profile;
        this.usernameText = profile.getName();
        this.sPhoneNumberText = profile.getPhoneNumber();
        this.profilePicPath = profile.getProfilePicPath();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel profileLabel = new JLabel("Profile");
        profileLabel.setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 24));
        profileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(profileLabel);

        String pathToLoad = profilePicPath.isEmpty()
                ? "messengerProgram/src/datastructProject/images/profilePicture.png"
                : profilePicPath;
        try {
            BufferedImage profileImage = ImageIO.read(new File(pathToLoad));
            Image scaledprofileImage = profileImage.getScaledInstance(250,250, Image.SCALE_DEFAULT);
            profileImageLabel = new JLabel(new ImageIcon(scaledprofileImage));
        } catch (IOException ex) {
            System.out.println("Error loading image");
        }
        profileImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(profileImageLabel);

        JButton profileButton = new JButton("Edit");
        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileButton.addActionListener(e -> changeProfilePicture());
        panel.add(profileButton);

        JPanel usernameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel usernameTitleLabel = new JLabel("Username:");
        usernameTitleLabel.setFont(font);
        usernameRow.add(usernameTitleLabel);

        usernameValueLabel = new JLabel(usernameText);
        usernameRow.add(usernameValueLabel);

        JButton usernameButton = new JButton("Edit");
        usernameButton.addActionListener(e -> { usernameText = editComponent("Username");
            if (!usernameText.isEmpty()) {
                profile.setName(usernameText);
                usernameValueLabel.setText(profile.getName());
            }
        });
        usernameRow.add(usernameButton);

        JPanel sPhoneNumberRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel phoneTitleLabel = new JLabel("Phone Number:");
        phoneTitleLabel.setFont(font);
        sPhoneNumberRow.add(phoneTitleLabel);

        phoneValueLabel = new JLabel(sPhoneNumberText);
        sPhoneNumberRow.add(phoneValueLabel);

        panel.add(usernameRow);
        panel.add(sPhoneNumberRow);

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Private helper method to edit a component and return the updated value
     *
     * @param type The type of component being edited (e.g., "Username", "Phone Number")
     * @return String The updated value entered by the user, or an empty string if cancelled 
     */
    private String editComponent(String type) 
    {
        JTextField field = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(1, 2, 8, 8));
        form.add(new JLabel(type + ":"));
        form.add(field);

        int result = JOptionPane.showConfirmDialog(this, form, "Edit " + type, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If statememt to check whether the user clicked OK or Cancel
        if (result == JOptionPane.OK_OPTION) 
            {
                String input = field.getText().trim();
                if (input.isEmpty()) 
                    {
                        // Show error message if the input is empty
                        JOptionPane.showMessageDialog(this, "Field cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        // Return empty string to indicate no change
                        return "";
                    }
                return input;
            }
        return "";
    }

    /**
     * Private method to change the profile picture
     */
    private void changeProfilePicture() 
    {
        JPanel form = new JPanel(new GridLayout(1, 2, 8, 8));
        form.add(new JLabel("Change Profile Picture:"));
        JButton upload = new JButton("Upload");
        upload.addActionListener(e -> changedPic());
        form.add(upload);

        int result = JOptionPane.showConfirmDialog(this, form, "Change Profile Picture", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Private helper method to handle the profile picture change process
     */ 
    private void changedPic() 
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(getParent());
        if (result == JFileChooser.APPROVE_OPTION) 
            {
            try {
                File file = fileChooser.getSelectedFile();
                profilePicPath = file.getAbsolutePath();
                profile.setProfilePicPath(profilePicPath);
                BufferedImage picture = ImageIO.read(file);
                Image scaledPicture = picture.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
                profileImageLabel.setIcon(new ImageIcon(scaledPicture));
                } catch (IOException ex) 
                {
                    // Print stack trace for debugging and show error message to user
                    ex.printStackTrace();
                    System.out.println("Error loading image");
                }
            }
    }

    /**
     * Method to refresh the profile information displayed on the page
     */
    public void refresh() 
    {
        usernameValueLabel.setText(profile.getName());
        phoneValueLabel.setText(profile.getPhoneNumber());
        
        // Update profile picture if needed
        String pathToLoad = profile.getProfilePicPath().isEmpty()
                ? "messengerProgram/src/datastructProject/images/profilePicture.png"
                : profile.getProfilePicPath();
        try {
            BufferedImage profileImage = ImageIO.read(new File(pathToLoad));
            Image scaledProfileImage = profileImage.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
            profileImageLabel.setIcon(new ImageIcon(scaledProfileImage));
        } catch (IOException ex) 
        {
            System.out.println("Error loading image");
        }
    }
}
