package datastructProject;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;

public class menu {
    private JFrame frame;

    public menu() throws IOException {
        frame = new JFrame("Text");

        frame.setSize(1000, 1000);
        frame.setTitle("Messenger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel title = new JLabel("Landing Page");
        title.setFont(new Font("Sans Serif", Font.BOLD, 36));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setBounds(0, 0, 1000, 80);
        frame.add(title);

        // dropdown menu options
        String[] pages = {"Home", "Contacts", "Search", "New Chat", "Edit Profile", "Save/Load"};
        JComboBox<String> dropdown = new JComboBox<>(pages);

        JPanel cards = new JPanel(new CardLayout());

        JPanel homePanel = new JPanel();
        homePanel.add(new JLabel("Home"));

        JPanel contactsPanel = new JPanel();
        contactsPanel.add(new JLabel("Contacts"));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search"));

        JPanel newChatPanel = new JPanel();
        newChatPanel.add(new JLabel("New Chat"));

        JPanel editProfilePanel = new JPanel();
        editProfilePanel.add(new JLabel("Edit Profile"));

        JPanel savePanel = new JPanel();
        savePanel.add(new JLabel("Save/Load"));

        cards.add(homePanel, "Home");
        cards.add(contactsPanel, "Contacts");
        cards.add(searchPanel, "Search");
        cards.add(newChatPanel, "New Chat");
        cards.add(editProfilePanel, "Edit Profile");
        cards.add(savePanel, "Save/Load");

        dropdown.addActionListener(e -> {
            CardLayout c1 = (CardLayout)(cards.getLayout());
            c1.show(cards, (String) dropdown.getSelectedItem());
        });

        // container panel that is moved down a bit
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(dropdown, BorderLayout.NORTH);
        topPanel.add(cards, BorderLayout.CENTER);
        containerPanel.add(topPanel, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(title, BorderLayout.NORTH);
        frame.add(containerPanel, BorderLayout.CENTER);
    }

    public void show() {
        frame.setVisible(true);
    }
    public static void main(String[] args) throws IOException {
        menu menu = new menu();
        menu.show();
    }
}