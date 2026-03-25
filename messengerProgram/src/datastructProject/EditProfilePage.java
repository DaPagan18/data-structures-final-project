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
    String aboutInputText = "";
    String fNameText = "John";
    String sNameText = "Doe";
    JLabel profileImageLabel = new JLabel();

    public EditProfilePage() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel profileLabel = new JLabel("Profile");
        profileLabel.setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 24));
        profileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(profileLabel);

        BufferedImage profileImage;
        try {
            profileImage = ImageIO.read(new File("messengerProgram/src/datastructProject/images/profilePicture.png"));
            profileImageLabel = new JLabel(new ImageIcon(profileImage));
            profileImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(profileImageLabel);
        } catch (IOException ex) {
            System.out.println("Error loading image");
        }

        JButton profileButton = new JButton("Edit");
        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileButton.addActionListener(e -> changeProfilePicture());
        panel.add(profileButton);


        JPanel aboutRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel aboutLabel = new JLabel("About:");
        aboutLabel.setFont(font);
        aboutRow.add(aboutLabel);

        JLabel aboutText = new JLabel(aboutInputText);
        aboutRow.add(aboutText);

        JButton aboutButton = new JButton("Edit");
        aboutButton.addActionListener(e -> { aboutInputText = editComponent("About"); 
            if (!aboutInputText.isEmpty()) {
                aboutText.setText(aboutInputText);
            }
        });
        aboutRow.add(aboutButton);

        JPanel fNameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel fNameLabel = new JLabel("First Name:");
        fNameLabel.setFont(font);
        fNameRow.add(fNameLabel);

        JLabel fName = new JLabel(fNameText);
        fNameRow.add(fName);

        JButton fNameButton = new JButton("Edit");
        fNameButton.addActionListener(e -> { fNameText = editComponent("First Name");
            if (!fNameText.isEmpty()) {
                fName.setText(fNameText);
            }
        });
        fNameRow.add(fNameButton);

        JPanel sNameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel sNameLabel = new JLabel("Surname:");
        sNameLabel.setFont(font);
        sNameRow.add(sNameLabel);

        JLabel sName = new JLabel(sNameText);
        sNameRow.add(sName);

        JButton sNameButton = new JButton("Edit");
        sNameButton.addActionListener(e -> { sNameText = editComponent("Surname");
            if (!sNameText.isEmpty()) {
                sName.setText(sNameText);
            }
        });
        sNameRow.add(sNameButton);

        panel.add(aboutRow);
        panel.add(fNameRow);
        panel.add(sNameRow);

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
                BufferedImage picture = ImageIO.read(file);
                profileImageLabel.setIcon(new ImageIcon(picture));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error loading image");
            }
        }
    }
}
