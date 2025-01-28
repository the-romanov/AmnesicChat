import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class CreateServer {
    static CreateAccount createAccount = CentralManager.getCreateAccount();
    static CipherData cipherData = CentralManager.getCipherData();
    static StorageDevices storageDevices = CentralManager.getStorageDevices();
    static {
        if (storageDevices == null) {
            System.err.println("CentralManager.getStorageDevices() returned null. Initializing StorageDevices manually.");
            storageDevices = new StorageDevices();
        }
    }
    
    private static String joinPassword;
    private static List<String> serverTypes = new ArrayList<String>();
    private static String serverFileName;
    private static String serverDirectory;
    private static List<String> whitelist;
    private static List<String> blacklist;
    private static int rateLimit;
    private static int portNumber;
    private static File unlockedFile;
    
    //Access the Hash instance
    static Hash hash = CentralManager.getHash();
    static HostServer hostServer = CentralManager.getHostServer();
    
    public static void createServer3(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Create A Server", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(titleLabel, gbc);

        // Bot Protection
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel botProtectionLabel = new JLabel("Bot Protection:");
        frame.add(botProtectionLabel, gbc);

        gbc.gridx = 1;
        JPanel botProtectionPanel = new JPanel(new FlowLayout());
        JRadioButton botYes = new JRadioButton("YES");
        JRadioButton botNo = new JRadioButton("NO");
        ButtonGroup botProtectionGroup = new ButtonGroup();
        botProtectionGroup.add(botYes);
        botProtectionGroup.add(botNo);
        botProtectionPanel.add(botYes);
        botProtectionPanel.add(botNo);
        frame.add(botProtectionPanel, gbc);

        // Captcha Options (only visible when Bot Protection is YES)
        gbc.gridy++;
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        JPanel captchaPanel = new JPanel(new FlowLayout());
        JCheckBox mathCaptcha = new JCheckBox("Math");
        JCheckBox wordsCaptcha = new JCheckBox("Words");
        JCheckBox patternCaptcha = new JCheckBox("Pattern");

        captchaPanel.add(mathCaptcha);
        captchaPanel.add(wordsCaptcha);
        captchaPanel.add(patternCaptcha);
        captchaPanel.setVisible(false); // Initially hidden, will show when Bot Protection is YES
        frame.add(captchaPanel, gbc);

        // Port Number
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel portNumberLabel = new JLabel("Port Number:");
        frame.add(portNumberLabel, gbc);

        gbc.gridx = 1;
        JTextField portNumberField = new JTextField("10619");
        frame.add(portNumberField, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        frame.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        frame.add(passwordField, gbc);

        // Device Selection
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel deviceLabel = new JLabel("Select Devices:");
        deviceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        frame.add(deviceLabel, gbc);

        gbc.gridy++;
        JPanel devicePanel = new JPanel(new GridLayout(0, 1, 10, 10));
        List<String> deviceNames = storageDevices.getStorageDeviceNames();
        Map<String, String> diskToSerialMap = new HashMap<>();
        List<String> selectedSerials = new ArrayList<>();

        for (String device : deviceNames) {
            diskToSerialMap.put(device, "Serial-" + device.hashCode());
            JToggleButton deviceToggleButton = new JToggleButton(device);

            deviceToggleButton.addActionListener(e -> {
                if (deviceToggleButton.isSelected()) {
                    selectedSerials.add(diskToSerialMap.get(device));
                } else {
                    selectedSerials.remove(diskToSerialMap.get(device));
                }
            });

            devicePanel.add(deviceToggleButton);
        }
        frame.add(devicePanel, gbc);

        // Encryption Algorithms
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel encryptionLabel = new JLabel("Select Encryption Algorithms:");
        encryptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        frame.add(encryptionLabel, gbc);

        gbc.gridy++;
        JPanel encryptionPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JCheckBox aesCheckbox = new JCheckBox("AES");
        JCheckBox serpentCheckbox = new JCheckBox("Serpent");
        JCheckBox twofishCheckbox = new JCheckBox("Twofish");
        JCheckBox camelliaCheckbox = new JCheckBox("Camellia");
        JCheckBox kuzCheckbox = new JCheckBox("Kuznyechik");

        encryptionPanel.add(aesCheckbox);
        encryptionPanel.add(serpentCheckbox);
        encryptionPanel.add(twofishCheckbox);
        encryptionPanel.add(camelliaCheckbox);
        encryptionPanel.add(kuzCheckbox);

        frame.add(encryptionPanel, gbc);

        // Encryption Order Display
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel encryptionOrderLabel = new JLabel("Selected Encryption Order:");
        encryptionOrderLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        frame.add(encryptionOrderLabel, gbc);

        gbc.gridy++;
        JTextArea encryptionOrderArea = new JTextArea(3, 20); // Display area for encryption order
        encryptionOrderArea.setEditable(false);
        encryptionOrderArea.setText(""); // Initially empty
        frame.add(new JScrollPane(encryptionOrderArea), gbc);

        // Action listeners to update encryption order text area
        ActionListener encryptionListener = e -> {
            List<String> selectedEncryptionMethods = new ArrayList<>();
            if (aesCheckbox.isSelected()) selectedEncryptionMethods.add("AES");
            if (serpentCheckbox.isSelected()) selectedEncryptionMethods.add("Serpent");
            if (twofishCheckbox.isSelected()) selectedEncryptionMethods.add("Twofish");
            if (camelliaCheckbox.isSelected()) selectedEncryptionMethods.add("Camellia");
            if (kuzCheckbox.isSelected()) selectedEncryptionMethods.add("Kuznyechik");

            // Update the encryption order display
            encryptionOrderArea.setText(String.join(", ", selectedEncryptionMethods)); // Show selected order in the text area
        };

        // Add action listeners to each checkbox
        aesCheckbox.addActionListener(encryptionListener);
        serpentCheckbox.addActionListener(encryptionListener);
        twofishCheckbox.addActionListener(encryptionListener);
        camelliaCheckbox.addActionListener(encryptionListener);
        kuzCheckbox.addActionListener(encryptionListener);

        // Continue Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton continueButton = new JButton("Continue");
        frame.add(continueButton, gbc);

        // Action Listener for Continue
     // Continue Button
     // Continue Button
        continueButton.addActionListener(e -> {
            String portNumberText = portNumberField.getText();
            String password = new String(passwordField.getPassword());
            String botProtection = botYes.isSelected() ? "YES" : "NO";

            // Validate Bot Protection
            if ("YES".equals(botProtection)) {
                // Check if at least one captcha method is selected
                if (!(mathCaptcha.isSelected() || wordsCaptcha.isSelected() || patternCaptcha.isSelected())) {
                    JOptionPane.showMessageDialog(frame, "Please select a Captcha method.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop execution if Captcha is not selected
                }
            }

            List<byte[]> keys = new ArrayList<>();

            // Hash the selected serials and add them to the key list
            if (!selectedSerials.isEmpty()) {
                List<String> hashedSerials = new ArrayList<>();
                for (String serial : selectedSerials) {
                    byte[] hashedBytes = hash.hashSHA256(serial);
                    hashedSerials.add(Base64.getEncoder().encodeToString(hashedBytes)); // Convert to Base64 string
                }

                // Join the Base64 encoded hashed serials into one string
                String serials = String.join(",", hashedSerials); // Join Base64 strings
                byte[] hashedSerialsBytes = hash.hashSHA256(serials);
                keys.add(hashedSerialsBytes);  // Add the byte[] directly
            }

            // Hash the password and add to the list as a byte[]
            byte[] passwordHashBytes = hash.hashSHA256(password);
            keys.add(passwordHashBytes); // Add the hashed password bytes directly

            // Collect selected encryption methods for the order
            ArrayList<String> encryptionOrder = new ArrayList<>();
            if (aesCheckbox.isSelected()) encryptionOrder.add("AES");
            if (serpentCheckbox.isSelected()) encryptionOrder.add("Serpent");
            if (twofishCheckbox.isSelected()) encryptionOrder.add("Twofish");
            if (camelliaCheckbox.isSelected()) encryptionOrder.add("Camellia");
            if (kuzCheckbox.isSelected()) encryptionOrder.add("Kuznyechik");

            // Update the encryption order display area
            encryptionOrderArea.setText(String.join(", ", encryptionOrder)); // Show selected order in the text area

            // Generate server key using cipherData.generateRandomKey(string)
            String serverKey = cipherData.generateRandomKey();

            // Hash the joinPassword before writing it to the file
            String hashedJoinPassword = Base64.getEncoder().encodeToString(hash.hashSHA256(password)); // Hash the joinPassword and convert to Base64

            // Get the formatted string to save to file
            String formattedData = String.format("%s:%s:%s:%d:%s:%s",
                    hashedJoinPassword, // Use hashed joinPassword here
                    String.join(",", whitelist),
                    String.join(",", blacklist),
                    rateLimit,
                    portNumberText,
                    serverKey);

            // Write the formatted data to a file
            try {
                String userHome = System.getProperty("user.home");
                unlockedFile = new File(userHome, "server_configuration.txt"); // Adjust the filename as necessary
                try (FileWriter writer = new FileWriter(unlockedFile)) {
                    writer.write(formattedData);
                }
                JOptionPane.showMessageDialog(frame, "Configuration saved successfully:\n" + unlockedFile.getAbsolutePath(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            // Encrypt the concatenated string (plain text)
            try {
                String concatenatedString = botProtection;
                byte[] concatenatedData = concatenatedString.getBytes(StandardCharsets.UTF_8);

                // Encrypt the data using the generated keys and encryption order
                File keyFile = new File(System.getProperty("user.home") + File.separator + "communication_key.txt");
                byte[] encryptedData = cipherData.encryptFileWithOrder(keyFile, keys, encryptionOrder);

                // Define the output directory and filename (using server name)
                String userHome = System.getProperty("user.home");
                File encryptedFile = new File(userHome, serverFileName + "_encrypted.txt");

                // Write the encrypted data to the file
                try (FileOutputStream fos = new FileOutputStream(encryptedFile)) {
                    fos.write(encryptedData);
                }

                JOptionPane.showMessageDialog(frame, "File created and encrypted successfully:\n" + encryptedFile.getAbsolutePath(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                hostServer.hostLiveServer(unlockedFile);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Bot Protection checkbox event handler to toggle captcha panel visibility
        botYes.addActionListener(e -> captchaPanel.setVisible(true));
        botNo.addActionListener(e -> captchaPanel.setVisible(false));

        // Frame Settings
        frame.setTitle("Create Server");
        frame.setSize(700, 800);
    }

	
    public static void createServer2(JFrame frame) {
        frame.getContentPane().removeAll();

        // Set the layout
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title Label
        JLabel titleLabel = new JLabel("Create A Server", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Whitelist IP Addresses
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel whitelistLabel = new JLabel("Whitelist IP Addresses:");
        frame.add(whitelistLabel, gbc);

        gbc.gridx = 1;
        JTextField whitelistField = new JTextField();
        frame.add(whitelistField, gbc);

        // Whitelist Info Label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel whitelistInfoLabel = new JLabel(
                "(if there is anything in there, it will assume all other addresses on the blacklist. To separate IP addresses, add a comma (,) for new IP address.)",
                SwingConstants.CENTER);
        whitelistInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        frame.add(whitelistInfoLabel, gbc);

        // Blacklist IP Addresses
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel blacklistLabel = new JLabel("Blacklist IP Addresses:");
        frame.add(blacklistLabel, gbc);

        gbc.gridx = 1;
        JTextField blacklistField = new JTextField();
        frame.add(blacklistField, gbc);

        // Rate Limit
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel rateLimitLabel = new JLabel("Rate Limit:");
        frame.add(rateLimitLabel, gbc);

        gbc.gridx = 1;
        JPanel rateLimitPanel = new JPanel(new BorderLayout());
        JTextField rateLimitField = new JTextField();
        JLabel rateLimitUnitLabel = new JLabel("MB/s");

        rateLimitPanel.add(rateLimitField, BorderLayout.CENTER);
        rateLimitPanel.add(rateLimitUnitLabel, BorderLayout.EAST);
        frame.add(rateLimitPanel, gbc);

        // Rate Limit Info Label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel rateLimitInfoLabel = new JLabel(
                "(A rate limit of 0 means unrestricted traffic, it is recommended to rate limit to prevent DDOS etc)",
                SwingConstants.CENTER);
        rateLimitInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        frame.add(rateLimitInfoLabel, gbc);

        // Continue Button
        gbc.gridy++;
        JButton continueButton = new JButton("Continue");
        frame.add(continueButton, gbc);

        // Add ActionListener for Continue Button
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String whitelistInput = whitelistField.getText();
                String blacklistInput = blacklistField.getText();
                String rateLimitInput = rateLimitField.getText();

                // Save to respective static variables
                whitelist = parseIpList(whitelistInput); // Save as list of IPs
                blacklist = parseIpList(blacklistInput); // Save as list of IPs

                try {
                    rateLimit = Integer.parseInt(rateLimitInput); // Save rate limit as integer

                    // Proceed to next step
                    createServer3(frame);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for Rate Limit.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Set the frame properties
        frame.setTitle("Create Server");
        frame.setSize(800, 500);
    }

    // Helper method to parse IP addresses from a comma-separated string
    private static List<String> parseIpList(String input) {
        List<String> ipList = new ArrayList<>();
        if (input != null && !input.trim().isEmpty()) {
            String[] ips = input.split(",");
            for (String ip : ips) {
                ipList.add(ip.trim());
            }
        }
        return ipList;
    }
	
    public static void createServer1(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Create A Server", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("Join Password:");
        frame.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField();
        frame.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("(you may leave this blank if you want to)", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        frame.add(infoLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel fileNameLabel = new JLabel("Server File Name:");
        frame.add(fileNameLabel, gbc);

        gbc.gridx = 1;
        JTextField fileNameField = new JTextField();
        frame.add(fileNameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel fileDirectoryLabel = new JLabel("Server File Directory:");
        frame.add(fileDirectoryLabel, gbc);

        gbc.gridx = 1;
        JPanel directoryPanel = new JPanel(new BorderLayout());
        JTextField directoryField = new JTextField("Default");
        JButton browseButton = new JButton("Browse");

        directoryPanel.add(directoryField, BorderLayout.CENTER);
        directoryPanel.add(browseButton, BorderLayout.EAST);
        frame.add(directoryPanel, gbc);

        // Server Types Selection
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel serverTypeLabel = new JLabel("Server Types:", SwingConstants.CENTER);
        frame.add(serverTypeLabel, gbc);

        gbc.gridy++;
        JPanel serverTypePanel = new JPanel();
        JCheckBox directoryCheck = new JCheckBox("DIRECTORY");
        JCheckBox chatCheck = new JCheckBox("CHAT");
        JCheckBox pingCheck = new JCheckBox("PING");

        serverTypePanel.add(directoryCheck);
        serverTypePanel.add(chatCheck);
        serverTypePanel.add(pingCheck);
        frame.add(serverTypePanel, gbc);

        gbc.gridy++;
        JButton continueButton = new JButton("Continue");
        frame.add(continueButton, gbc);

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                directoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        continueButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            String fileName = fileNameField.getText();
            String directory = directoryField.getText();

            serverTypes.clear();
            if (directoryCheck.isSelected()) serverTypes.add("DIRECTORY");
            if (chatCheck.isSelected()) serverTypes.add("CHAT");
            if (pingCheck.isSelected()) serverTypes.add("PING");

            if (fileName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a server file name.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                createServer2(frame);
            }
        });

        frame.setTitle("Create Server");
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

}