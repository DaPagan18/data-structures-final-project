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

public class EditProfilePage extends JPanel {
    Font font = new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 18);

  
    Profile profile;
    String fNameText;
    String sPhoneNumberText;
    String profilePicPath;
    
    JLabel profileImageLabel = new JLabel();

    public EditProfilePage(Profile profile) {
        this.profile = profile;
        this.fNameText = profile.getName();
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
            profileImageLabel = new JLabel(new ImageIcon(profileImage));
        } catch (IOException ex) {
            System.out.println("Error loading image");
        }
        profileImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(profileImageLabel);

        JButton profileButton = new JButton("Edit");
        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileButton.addActionListener(e -> changeProfilePicture());
        panel.add(profileButton);

        JPanel fNameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel fNameLabel = new JLabel("First Name:");
        fNameLabel.setFont(font);
        fNameRow.add(fNameLabel);

        JLabel fName = new JLabel(fNameText);
        fNameRow.add(fName);

        JButton fNameButton = new JButton("Edit");
        fNameButton.addActionListener(e -> { fNameText = editComponent("First Name");
            if (!fNameText.isEmpty()) {
                profile.setName(fNameText);
                fName.setText(profile.getName());
            }
        });
        fNameRow.add(fNameButton);

        JPanel sPhoneNumberRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel sPhoneNumberLabel = new JLabel("Phone Number:");
        sPhoneNumberLabel.setFont(font);
        sPhoneNumberRow.add(sPhoneNumberLabel);

        JLabel sPhoneNumber = new JLabel(sPhoneNumberText);
        sPhoneNumberRow.add(sPhoneNumber);

        JButton sPhoneNumberButton = new JButton("Edit");
        sPhoneNumberButton.addActionListener(e -> { sPhoneNumberText = editComponent("Phone Number");
            if (!sPhoneNumberText.isEmpty()) {
                profile.setPhoneNumber(sPhoneNumberText);
                sPhoneNumber.setText(profile.getPhoneNumber());
            }
        });
        sPhoneNumberRow.add(sPhoneNumberButton);

        panel.add(fNameRow);
        panel.add(sPhoneNumberRow);

        add(panel, BorderLayout.CENTER);
    }

    private String editComponent(String type) {
        JTextField field = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(1, 2, 8, 8));
        form.add(new JLabel(type + ":"));
        form.add(field);

        int result = JOptionPane.showConfirmDialog(this, form, "Edit " + type, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String input = field.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Field cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }
            return input;
        }
        return "";
    }

    private void changeProfilePicture() {
        JPanel form = new JPanel(new GridLayout(1, 2, 8, 8));
        form.add(new JLabel("Change Profile Picture:"));
        JButton upload = new JButton("Upload");
        upload.addActionListener(e -> changedPic());
        form.add(upload);


        int result = JOptionPane.showConfirmDialog(this, form, "Change Profile Picture", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {

        }
    }
    
    public void changedPic() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(getParent());
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                profilePicPath = file.getAbsolutePath();
                profile.setProfilePicPath(profilePicPath);
                BufferedImage picture = ImageIO.read(file);
                profileImageLabel.setIcon(new ImageIcon(picture));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error loading image");
            }
        }
    }
}
