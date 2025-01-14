import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JButton;

public class App {
	
	/*
	 * 
	 * The App class is the main class.
	 * 
	 * It will be the root for running functions like:
	 * - Hash
	 * - CreateAccount
	 * 
	 * The App exists to not only make the entire program
	 * modular but to also allow easy amendments where
	 * necessary.
	 * 
	 * The App functions like a fallback if anything else.
	 * If a server shuts down or a Peer-to-Peer session
	 * ends, the user will be redirected here.
	 * 
	 * */
	
	//UI Defaults
	public URL fileButtonIconURL = getClass().getResource("/images/File.png");
	public URL labelURL = getClass().getResource("/images/AmnesicLabel.png");
    private JFrame frame; //JFrame is private so that we can isolate the variable to prevent potential tampering.
    public JPanel appPanel = new JPanel(); // Stops creating new panels unnecessarily.
    public int baseWidth = 650;
    public int baseHeight = 350;	
    
    public App() {
        frame = new JFrame("AmnesicChat"); // Constructor for frame
    }

    public JFrame getJFrame() {
        return frame; // Get private frame only
    }
    
    //Import from CentralManager
    static CreateServer createServer = CentralManager.getCreateServer();
    
    // Access the Hash instance
    static Hash hash = CentralManager.getHash();
    
    // Access the CreateAccount instance
    static CreateAccount createAccount = CentralManager.getCreateAccount();
    
    //Access the JoinServer instance
    static JoinServer joinServer = CentralManager.getJoinServer();
    
    //Access the Peer To Peer instance
    static JoinPeerToPeer peerToPeer = CentralManager.getJoinPeerToPeer();
    
    // Variables for account creation
    public boolean strictMode = false; // Enforce strict account protection
    public List<String> hashedSerials = null; //Device ID encryption key

    public void loggedInMenu(JFrame frame, String username, String publicFingerprint) {
        // Clear frame
        frame.getContentPane().removeAll();

        // Set frame size
        frame.setSize(800, 650);

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(null); // Using null layout for custom positioning

        // Calculate center points
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();

        // Add AmnesicChat label
        JLabel imageLabel = new JLabel();
        if (labelURL != null) {
            ImageIcon originalIcon = new ImageIcon(labelURL);
            imageLabel.setIcon(originalIcon);
        } else {
            System.out.println("Image not found!");
        }
        int imageLabelWidth = 650;
        int imageLabelHeight = 120;
        imageLabel.setBounds((frameWidth - imageLabelWidth) / 2, 20, imageLabelWidth, imageLabelHeight);
        panel.add(imageLabel);

        // Add username label
        JLabel usernameLabel = new JLabel("Username: " + username + " (change)");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        int usernameLabelWidth = 400;
        int usernameLabelHeight = 20;
        usernameLabel.setBounds((frameWidth - usernameLabelWidth) / 2, 160, usernameLabelWidth, usernameLabelHeight);
        panel.add(usernameLabel);

        // Add fingerprint label
        JLabel fingerprintLabel = new JLabel("Fingerprint: " + publicFingerprint);
        fingerprintLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        int fingerprintLabelWidth = 400;
        int fingerprintLabelHeight = 20;
        fingerprintLabel.setBounds((frameWidth - fingerprintLabelWidth) / 2, 190, fingerprintLabelWidth, fingerprintLabelHeight);
        panel.add(fingerprintLabel);

        // Add fingerprint info label
        JLabel fingerprintInfo = new JLabel("<html>This fingerprint above is the public fingerprint. It can be shared with others to add you as a contact, just like saving a phone number.</html>");
        fingerprintInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        int fingerprintInfoWidth = 550;
        int fingerprintInfoHeight = 40; // Adjusted for multi-line text
        fingerprintInfo.setBounds((frameWidth - fingerprintInfoWidth) / 2, 220, fingerprintInfoWidth, fingerprintInfoHeight);
        panel.add(fingerprintInfo);

        // Create "Copy Fingerprint" button
        JButton copyFingerprintButton = new JButton("Copy Fingerprint");
        copyFingerprintButton.setFont(new Font("Arial", Font.PLAIN, 14));
        int buttonWidth = 150;
        int buttonHeight = 30;
        copyFingerprintButton.setBounds((frameWidth - buttonWidth) / 2, 260, buttonWidth, buttonHeight);

        // Action listener to copy fingerprint to clipboard
        copyFingerprintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(publicFingerprint);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);

                JOptionPane.showMessageDialog(frame, "Fingerprint copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add the "Copy Fingerprint" button to the panel
        panel.add(copyFingerprintButton);

        // Add buttons
        String[] buttonLabels = {"JOIN A SERVER", "PEER TO PEER", "HOST A SERVER", "CHANGE ACCOUNT", "QUIT"};
        int buttonSpacing = 40;
        int initialYPosition = 300; // Starting y-position for the other buttons

        for (String text : buttonLabels) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBounds((frameWidth - buttonWidth) / 2, initialYPosition, buttonWidth, buttonHeight);

            // Action listener for "Quit" button
            if (text.equals("QUIT")) {
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0); // Exit the program
                    }
                });
            }

            // Action listener for "JOIN A SERVER" button
            if (text.equals("JOIN A SERVER")) {
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        joinServer.createJoinServerUI(frame);
                    }
                });
            }

            // Action listener for "PEER TO PEER" button
            if (text.equals("PEER TO PEER")) {
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        peerToPeer.peerToPeerUI(frame); // Call peerToPeerUI
                    }
                });
            }

            // Action listener for "CHANGE ACCOUNT" button
            if (text.equals("CHANGE ACCOUNT")) {
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mainMenu(frame); // Call the mainMenu method to return to the main menu
                    }
                });
            }

            // Action listener for "HOST A SERVER" button
            if (text.equals("HOST A SERVER")) {
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Create an instance of CreateServer
                        CreateServer createServer = new CreateServer();

                        // Call createServer1 method
                        createServer.createServer1(frame);
                    }
                });
            }

            panel.add(button);
            initialYPosition += buttonHeight + buttonSpacing;
        }

        // Add settings gear icon at the top right of the screen
        JLabel settingsLabel = new JLabel();
        ImageIcon gearIcon = new ImageIcon("/images/gear.png"); // Load your gear icon image
        settingsLabel.setIcon(gearIcon);
        int settingsIconSize = 30;
        settingsLabel.setBounds(frame.getWidth() - settingsIconSize - 20, 20, settingsIconSize, settingsIconSize); // Positioned top-right
        panel.add(settingsLabel);

        // Add panel to frame
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    public void mainMenu(JFrame frame) {
        // Ensure this method runs on EDT (Event Dispatch Thread for stability of program)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Clear the current frame content
                frame.getContentPane().removeAll();

                // Set up the new layout
                frame.setTitle("AmnesicChat - Account");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(650, 450);
                frame.setLayout(new BorderLayout());

                try {
                    // Load the favicon image from the resources folder
                    URL faviconURL = getClass().getResource("/images/Favicon.png");
                    if (faviconURL != null) {
                        ImageIcon favicon = new ImageIcon(faviconURL);
                        frame.setIconImage(favicon.getImage());
                    } else {
                        System.out.println("Favicon not found");
                    }
                } catch (Exception e) {
                    System.out.println("Error loading favicon: " + e.getMessage());
                }

                // Main panel setup
                JPanel appPanel = new JPanel();
                appPanel.setLayout(new BoxLayout(appPanel, BoxLayout.Y_AXIS));
                appPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
                frame.add(appPanel, BorderLayout.CENTER);

                // Banner setup
                JLabel imageLabel = new JLabel();
                URL labelURL = getClass().getResource("/images/AmnesicLabel.png");
                if (labelURL != null) {
                    ImageIcon originalIcon = new ImageIcon(labelURL);
                    imageLabel.setIcon(originalIcon);
                } else {
                    System.out.println("Image not found!");
                }
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                appPanel.add(imageLabel);

                // Header label
                JLabel headerLabel = new JLabel("Create Account", SwingConstants.CENTER);
                headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
                headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                appPanel.add(headerLabel);

                appPanel.add(Box.createVerticalStrut(10)); // Add spacing

                // Instructions label
                JLabel instructionLabel = new JLabel(
                        "<html>If you have an existing account, use the directory selection below to open your account file. Otherwise, create a new account with the bottom button.</html>",
                        SwingConstants.CENTER);
                instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                appPanel.add(instructionLabel);

                appPanel.add(Box.createVerticalStrut(20)); // Add spacing

                // File chooser panel
                JPanel fileChooserPanel = new JPanel();
                fileChooserPanel.setLayout(new BoxLayout(fileChooserPanel, BoxLayout.X_AXIS));

                // Create the file path field
                JTextField filePathField = new JTextField();

                // Create Account / Load Account button
                JButton createAccountButton = new JButton("Create Account");
                createAccountButton.setPreferredSize(new Dimension(200, 40));
                createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                createAccountButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String filePath = filePathField.getText();
                        if (filePath.isEmpty()) {
                            createAccount.createAccount(frame); // Call create account logic
                        } else {
                            loadAccount(filePath, frame); // Call load account logic
                        }
                    }
                });
                
                // Create the browse button with resized image icon
                URL fileButtonIconURL = getClass().getResource("/images/File.png");
                if (fileButtonIconURL != null) {
                    ImageIcon originalIcon = new ImageIcon(fileButtonIconURL);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Resize image to 50x50
                    ImageIcon resizedIcon = new ImageIcon(scaledImage);

                    JButton browseButton = new JButton(resizedIcon);
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
                                // Change the button text to "Load Account" when a file is selected
                                createAccountButton.setText("Load Account");
                            }
                        }
                    });

                    // Add the components of file choosing to the main panel
                    appPanel.add(fileChooserPanel);
                } else {
                    System.out.println("File button icon not found");
                }

                appPanel.add(Box.createVerticalStrut(20)); // Add spacing

                appPanel.add(createAccountButton);

                // Back button to return to the main menu
                JButton backButton = new JButton("Back");
                backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                backButton.addActionListener(e -> mainMenu(frame)); // This will call the mainMenu again
                appPanel.add(backButton);

                frame.revalidate();  // Revalidate to ensure everything is laid out properly
                frame.repaint();  // Repaint to show the changes
            }
        });
    }

    private void loadAccount(String filePath, JFrame frame) {
        // Clear frame
        frame.getContentPane().removeAll();
        frame.setSize(600, 600);  // Adjust the size to match your previous layout
        frame.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Space around the header

        JLabel headerLabel = new JLabel("Enter Password to Load Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(headerLabel);

        JLabel descriptionLabel = new JLabel("<html>Please enter your password to decrypt and load your account.</html>",
                SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(descriptionLabel);

        frame.add(headerPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);  // Spacing between elements

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(250, 30));  // Set the width of the password field
        centerPanel.add(passwordField, gbc);

        // Add a gap
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        centerPanel.add(Box.createVerticalStrut(10), gbc);

        // Load Button
        JButton loadButton = new JButton("Load Account");
        loadButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loadButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            if (password != null && !password.isEmpty()) {
                try {
                    // Load the encrypted account file
                    File encryptedFile = new File(filePath);
                    byte[] encryptedData = Files.readAllBytes(encryptedFile.toPath());

                    // Decrypt the file (reverse the encryption order)
                    ArrayList<String> decryptionOrder = getDecryptionOrder(); // This should be your decryption order (reverse of encryption order)

                    // Decrypt the data using the entered password
                    byte[] decryptedData = decryptFileWithOrder(encryptedData, password, decryptionOrder);

                    // Now parse the decrypted data
                    String decryptedString = new String(decryptedData);
                    String[] accountData = decryptedString.split(":");

                    if (accountData.length >= 3) {
                        String username = accountData[0];
                        String description = accountData[1];
                        String communicationKey = accountData[2];
                        // Do something with these values
                        JOptionPane.showMessageDialog(frame, "Account loaded successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid account data", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Decryption failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Password is required to load the account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        centerPanel.add(loadButton, gbc);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));  // Use FlowLayout.CENTER for horizontal centering
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Padding

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            mainMenu(frame);  // Call selectSecurityModules() when Back button is clicked
        });
        footerPanel.add(backButton);

        frame.add(footerPanel, BorderLayout.SOUTH);

        // Refresh the frame to display the new layout
        frame.revalidate();
        frame.repaint();
    }

    private ArrayList<String> getDecryptionOrder() {
        ArrayList<String> decryptionOrder = new ArrayList<>();
        return decryptionOrder;
    }

    private byte[] decryptFileWithOrder(byte[] encryptedData, String password, ArrayList<String> decryptionOrder) throws Exception {
        // Decrypt the file data using the order of algorithms
        byte[] decryptedData = encryptedData;
        for (String algorithm : decryptionOrder) {
            switch (algorithm) {
                case "AES":
                    decryptedData = decryptWithAES(decryptedData, password);
                    break;
                case "Serpent":
                    decryptedData = decryptWithSerpent(decryptedData, password);
                    break;
                case "Twofish":
                    decryptedData = decryptWithTwofish(decryptedData, password);
                    break;
                case "Camellia":
                    decryptedData = decryptWithCamellia(decryptedData, password);
                    break;
                case "Kuznyechik":
                    decryptedData = decryptWithKuznyechik(decryptedData, password);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown decryption algorithm: " + algorithm);
            }
        }
        return decryptedData;
    }

    // AES Decryption
    private byte[] decryptWithAES(byte[] data, String password) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    // Serpent Decryption (Example with BouncyCastle)
    private byte[] decryptWithSerpent(byte[] data, String password) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("Serpent", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "Serpent");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    // Twofish Decryption (Example with BouncyCastle)
    private byte[] decryptWithTwofish(byte[] data, String password) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("Twofish", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "Twofish");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    // Camellia Decryption (Example with BouncyCastle)
    private byte[] decryptWithCamellia(byte[] data, String password) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("Camellia", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "Camellia");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    // Kuznyechik Decryption (Example with BouncyCastle)
    private byte[] decryptWithKuznyechik(byte[] data, String password) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("GOST3412-2015", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "GOST3412-2015");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }


    public static void main(String[] args) {
    	//We run the program through EventQueue with EDT (Event Dispatch Thread) to make program stable.
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    App window = new App();
                    window.mainMenu(window.getJFrame());
                    window.getJFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
