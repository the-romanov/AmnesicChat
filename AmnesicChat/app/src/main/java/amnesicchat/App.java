import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Arrays;
import javax.swing.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.IOException;

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
        frame.setSize(800, 750);

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
        try {
            // Load file content
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

            // Check if the content is plain text (ASCII)
            if (isASCII(fileContent)) {
                // Convert the byte array to string (UTF-8 encoding)
                String text = new String(fileContent, StandardCharsets.UTF_8);

                // Split the text based on the colon ":"
                String[] parts = text.split(":");

                if (parts.length >= 3) {
                    // Extract username and communication key
                    String username = parts[0];  // First part is username
                    String communicationKey = parts[2];  // Third part is the communication key

                    // Hash the communication key twice using SHA-512
                    String pubKey = hash.hashSHA512(hash.hashSHA512(communicationKey));

                    // Call loggedInMenu with the parsed username and pubKey
                    loggedInMenu(frame, username, "GPG Key Needed");
                } else {
                    // Handle error if the format doesn't match the expected pattern
                    JOptionPane.showMessageDialog(frame, "Invalid format in the file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return; // Exit the method to prevent further UI setup
            }

            // If the file is not plain text, proceed to decryption UI
            frame.getContentPane().removeAll();
            frame.setSize(600, 600);
            frame.setLayout(new BorderLayout());

            // Header Panel
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel headerLabel = new JLabel("Decrypt Account", SwingConstants.CENTER);
            headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(headerLabel);

            JLabel descriptionLabel = new JLabel(
                    "<html>If the file is encrypted, enter the password and decryption order.<br>"
                            + "If the file contains plain text or ASCII, it will be loaded automatically.</html>",
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
            gbc.insets = new Insets(15, 15, 15, 15);

            // Password Field
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            centerPanel.add(passwordLabel, gbc);

            gbc.gridx = 2;
            gbc.gridy = 4;
            gbc.gridwidth = 3;
            JPasswordField passwordField = new JPasswordField(20);
            passwordField.setPreferredSize(new Dimension(250, 30));
            centerPanel.add(passwordField, gbc);

            // Encryption Methods Panel
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            JLabel encryptionLabel = new JLabel("Encryption Method(s):");
            encryptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            centerPanel.add(encryptionLabel, gbc);

            JPanel encryptionMethodsPanel = new JPanel();
            encryptionMethodsPanel.setLayout(new GridLayout(3, 2, 10, 10));
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

            // Add encryption methods to order list
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
            footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
            footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
            backButton.addActionListener(e -> mainMenu(frame));

            JButton continueButton = new JButton("Continue");
            continueButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
            continueButton.addActionListener(e -> {
                try {
                    char[] pass = passwordField.getPassword();
                    if (pass.length == 0) {
                        JOptionPane.showMessageDialog(frame, "Password is required for decryption.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String passString = new String(pass);
                    byte[] passwordHash = hash.hashSHA256(passString);

                    // Get decryption order from the selected items in the order list
                    ArrayList<String> selectedOrder = new ArrayList<>();
                    for (int i = 0; i < orderListModel.size(); i++) {
                        selectedOrder.add(orderListModel.getElementAt(i));
                    }
                    Collections.reverse(selectedOrder); // Reverse for decryption order

                    byte[] decryptedData = decryptFileWithOrder(fileContent, passwordHash, selectedOrder);
                    String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);

                    // Process decrypted text for username and pubKey
                    String[] parts = decryptedText.split(":");
                    if (parts.length >= 3) {
                        String username = parts[0];  // First part is username
                        String communicationKey = parts[2];  // Third part is the communication key

                        // Hash the communication key twice using SHA-512
                        String pubKey = hash.hashSHA512(hash.hashSHA512(communicationKey));

                        // Call loggedInMenu with the parsed username and pubKey
                        loggedInMenu(frame, username, "GPG Key Needed");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid format in the decrypted file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            footerPanel.add(backButton);
            footerPanel.add(Box.createHorizontalStrut(10));
            footerPanel.add(continueButton);

            frame.add(footerPanel, BorderLayout.SOUTH);
            frame.revalidate();
            frame.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper function to determine if file content is ASCII
    private boolean isASCII(byte[] data) {
        for (byte b : data) {
            if (b < 0 || b > 127) return false; // Outside ASCII range
        }
        return true;
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
    private byte[] decryptWithAES(byte[] data, byte[] password) throws Exception {
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
