import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
import java.nio.file.Paths;
import java.nio.file.Files;
import javax.swing.JButton;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileOutputStream;

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

                frame.revalidate();  // Revalidate to ensure everything is laid out properly
                frame.repaint();  // Repaint to show the changes
            }
        });
    }

    private byte[] getEncryptedData(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));  // Read encrypted file content as byte array
    }
    
    private void loadAccount(String filePath, JFrame frame) {
        // Clear frame
        frame.getContentPane().removeAll();
        frame.setSize(600, 800);
        frame.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // More space around the header

        JLabel headerLabel = new JLabel("Create Password", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(headerLabel);

        JLabel descriptionLabel = new JLabel("<html>If you lose or forget the password, you will lose access to your account.<br>"
                + "Keep the password extremely safe! Nobody will help you to recover your account once you lose the password.</html>",
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
        gbc.insets = new Insets(15, 15, 15, 15);  // More spacing between elements

        // Password Minimum Length Notice
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5; // Span the full width
        JLabel minCharLabel = new JLabel("Password must not be empty.");
        minCharLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        minCharLabel.setForeground(Color.RED); // Optional: Make the notice more prominent
        centerPanel.add(minCharLabel, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(250, 30));  // Increase the width of the password field
        centerPanel.add(passwordField, gbc);

        // Encryption Methods
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JLabel encryptionLabel = new JLabel("Encryption Method(s):");
        encryptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(encryptionLabel, gbc);

        // Encryption Methods Panel with more spacing
        JPanel encryptionMethodsPanel = new JPanel();
        encryptionMethodsPanel.setLayout(new GridLayout(3, 2, 10, 10));  // Adjusted for better spacing
        JCheckBox aesCheckbox = new JCheckBox("AES");
        JCheckBox serpentCheckbox = new JCheckBox("Serpent");
        JCheckBox twofishCheckbox = new JCheckBox("Twofish");
        JCheckBox camelliaCheckbox = new JCheckBox("Camellia");
        JCheckBox kuzCheckbox = new JCheckBox("Kuznyechik");

        encryptionMethodsPanel.add(aesCheckbox);
        encryptionMethodsPanel.add(serpentCheckbox);
        encryptionMethodsPanel.add(twofishCheckbox);
        encryptionMethodsPanel.add(camelliaCheckbox);
        encryptionMethodsPanel.add(kuzCheckbox);

        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        centerPanel.add(encryptionMethodsPanel, gbc);

        // Encryption Order
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JLabel orderLabel = new JLabel("Encryption Order:");
        orderLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(orderLabel, gbc);

        DefaultListModel<String> orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        orderList.setVisibleRowCount(5);
        orderList.setFixedCellHeight(20);
        orderList.setFixedCellWidth(100);
        JScrollPane scrollPane = new JScrollPane(orderList);

        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        centerPanel.add(scrollPane, gbc);

        // Add encryption methods to order lista
        ActionListener addToOrder = e -> {
            JCheckBox source = (JCheckBox) e.getSource();
            if (source.isSelected()) {
                orderListModel.addElement(source.getText());
            } else {
                orderListModel.removeElement(source.getText());
            }
        };

        aesCheckbox.addActionListener(addToOrder);
        serpentCheckbox.addActionListener(addToOrder);
        twofishCheckbox.addActionListener(addToOrder);
        camelliaCheckbox.addActionListener(addToOrder);
        kuzCheckbox.addActionListener(addToOrder);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));  // Use FlowLayout.CENTER for horizontal centering
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Increased padding

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            mainMenu(frame);
        });

     // Continue Button
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // Ensuring button is centered
        continueButton.addActionListener(e -> {
            // Get the password from the field
            char[] pass = passwordField.getPassword();
            String passString = new String(pass);

            // Hash the password using SHA-256
            byte[] passwordHash = hash.hashSHA256(passString);

            // Get the selected encryption methods in order
            List<String> selectedOrder = orderList.getSelectedValuesList();

            // Reverse the selected order to get the decryption order
            ArrayList<String> decryptionOrder = new ArrayList<>(selectedOrder);
            Collections.reverse(decryptionOrder); // Reverse the order to match decryption logic

            // Example encrypted data (you would replace this with actual data to decrypt)
            byte[] encryptedData = null;
            try {
                encryptedData = getEncryptedData(filePath); // Load the encrypted data
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to load encrypted data.");
                return;
            }

            try {
                // Decrypt the data using the decryption order
                byte[] decryptedData = decryptFileWithOrder(encryptedData, passwordHash, decryptionOrder);

                // Convert decrypted data to a string assuming it was originally a text string
                String decryptedText = new String(decryptedData, StandardCharsets.UTF_8); // Decode with UTF-8

                // Print or display the decrypted text
                System.out.println("Decrypted Data: " + decryptedText);  // Prints decrypted text
                JOptionPane.showMessageDialog(frame, "Decrypted Data: \n" + decryptedText);

                // You can add logic to handle the decrypted data, such as saving it or displaying it.
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Decryption failed: " + ex.getMessage());
            }
        });


        // Add buttons to footer panel (Back and Continue)
        footerPanel.add(backButton);
        footerPanel.add(Box.createHorizontalStrut(10)); // Spacer between buttons
        footerPanel.add(continueButton);

        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }


    // Decryption Functions
    private ArrayList<String> getDecryptionOrder() {
        ArrayList<String> decryptionOrder = new ArrayList<>();
        // Add algorithms in reverse of encryption order
        decryptionOrder.add("Kuznyechik");
        decryptionOrder.add("Camellia");
        decryptionOrder.add("Twofish");
        decryptionOrder.add("Serpent");
        decryptionOrder.add("AES");
        return decryptionOrder;
    }

 // Decrypt Function with dynamic decryption order
    private byte[] decryptFileWithOrder(byte[] encryptedData, byte[] password, ArrayList<String> decryptionOrder) throws Exception {
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

    private void handleDecryptedData(byte[] decryptedData) {
        // Convert decrypted data to a string assuming it was originally a text string
        try {
            String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
            System.out.println("Decrypted Data: " + decryptedString);  // Prints decrypted text
        } catch (Exception e) {
            System.err.println("Error converting decrypted data: " + e.getMessage());
        }
    }

    // AES Decryption
    private byte[] decryptWithEAS(byte[] data, byte[] password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    // Other decryption methods remain unchanged
    private byte[] decryptWithSerpent(byte[] data, byte[] password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("Serpent", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password, "Serpent");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    private byte[] decryptWithTwofish(byte[] data, byte[] password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("Twofish", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password, "Twofish");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    private byte[] decryptWithCamellia(byte[] data, byte[] password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("Camellia", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password, "Camellia");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    private byte[] decryptWithKuznyechik(byte[] data, byte[] password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("GOST3412-2015", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(password, "GOST3412-2015");
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
