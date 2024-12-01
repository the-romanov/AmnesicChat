import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.filechooser.FileNameExtensionFilter;


public class App {

	public static void main(String[] args) {
        // Initialize the frame
        JFrame frame = new JFrame("Main Menu");
        frame.setTitle("AmnesicChat - Account");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 400);
        frame.setLayout(new BorderLayout());

        // Set icon for the frame
        try {
            ImageIcon favicon = new ImageIcon("images/Favicon.png"); // Replace with actual file path
            frame.setIconImage(favicon.getImage());
        } catch (Exception e) {
            System.out.println("Favicon not found");
        }

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Banner setup
        JLabel imageLabel = new JLabel();
        //try {
        URL imageURL = App.class.getResource("/images/AmnesicLabel.png");
        if (imageURL != null) {
            ImageIcon originalIcon = new ImageIcon(imageURL);
            imageLabel.setIcon(originalIcon);
        } else {
            System.out.println("Image not found!");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(imageLabel);

        // Header label
        JLabel headerLabel = new JLabel("Create Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(headerLabel);

        mainPanel.add(Box.createVerticalStrut(10)); // Add spacing

        // Instructions label
        JLabel instructionLabel = new JLabel(
            "<html>If you have an existing account, use the directory selection below to open your account file. Otherwise, create a new account with the bottom button.</html>",
            SwingConstants.CENTER
        );
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(instructionLabel);

        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing

        // File chooser panel
        JPanel fileChooserPanel = new JPanel();
        fileChooserPanel.setLayout(new BoxLayout(fileChooserPanel, BoxLayout.X_AXIS));

        // Create the file path field
        JTextField filePathField = new JTextField();
        
        // Create the browse button
        JButton browseButton = new JButton(new ImageIcon("images/File.png")); // Replace with actual file path
        browseButton.setPreferredSize(new Dimension(50, 50)); // Set fixed dimensions for the button

        // Set the height of the file path field to match the browse button
        filePathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, browseButton.getPreferredSize().height));

        // Add components to the panel
        fileChooserPanel.add(filePathField);
        fileChooserPanel.add(Box.createHorizontalStrut(10)); // Add spacing
        fileChooserPanel.add(browseButton);

        // Add action listener for browse button
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        
        //Add the components of file choosing to the main panel.
        fileChooserPanel.add(Box.createHorizontalStrut(10));
        fileChooserPanel.add(browseButton);

        //Disply the file chooser to the user
        mainPanel.add(fileChooserPanel);
        
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing

        // Create Account button
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setPreferredSize(new Dimension(200, 40));
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Creating a new account!");
            }
        });
        mainPanel.add(createAccountButton);

        // Set frame visible. We must set this otherwise our UI will be hidden.
        frame.setVisible(true);
    }
}
