import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class CreateServer {
    static CreateAccount createAccount = CentralManager.getCreateAccount();
    static CipherData cipherData = CentralManager.getCipherData();
    static StorageDevices storageDevices = CentralManager.getStorageDevices();

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

        // Captcha Type
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel captchaTypeLabel = new JLabel("Captcha Type:");
        frame.add(captchaTypeLabel, gbc);

        gbc.gridx = 1;
        JPanel captchaPanel = new JPanel(new FlowLayout());
        JButton mathButton = new JButton("MATH");
        JButton wordsButton = new JButton("WORDS");
        JButton patternsButton = new JButton("PATTERNS");
        captchaPanel.add(mathButton);
        captchaPanel.add(wordsButton);
        captchaPanel.add(patternsButton);
        frame.add(captchaPanel, gbc);

        // Bot Protection Info Label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel botProtectionInfoLabel = new JLabel("(Captcha will be requested before log on to server)", SwingConstants.CENTER);
        botProtectionInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        frame.add(botProtectionInfoLabel, gbc);

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

        // Encryption Methods
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JLabel encryptionLabel = new JLabel("Encryption Methods:");
        frame.add(encryptionLabel, gbc);

        gbc.gridx = 1;
        JPanel encryptionMethodsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
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
        frame.add(encryptionMethodsPanel, gbc);

        // Decryption Order
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel orderLabel = new JLabel("Decryption Order:");
        frame.add(orderLabel, gbc);

        gbc.gridx = 1;
        DefaultListModel<String> orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        JScrollPane scrollPane = new JScrollPane(orderList);
        frame.add(scrollPane, gbc);

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

        // Continue Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton continueButton = new JButton("Continue");
        frame.add(continueButton, gbc);

        // Action Listener for Continue
        continueButton.addActionListener(e -> {
            String portNumber = portNumberField.getText();
            String password = new String(passwordField.getPassword());
            String botProtection = botYes.isSelected() ? "YES" : "NO";
            List<String> encryptionMethods = new ArrayList<>();
            if (aesCheckbox.isSelected()) encryptionMethods.add("AES");
            if (serpentCheckbox.isSelected()) encryptionMethods.add("Serpent");
            if (twofishCheckbox.isSelected()) encryptionMethods.add("Twofish");
            if (camelliaCheckbox.isSelected()) encryptionMethods.add("Camellia");
            if (kuzCheckbox.isSelected()) encryptionMethods.add("Kuznyechik");

            JOptionPane.showMessageDialog(frame,
                    "Port Number: " + portNumber + "\nPassword: " + password + "\nBot Protection: " + botProtection +
                            "\nEncryption Methods: " + encryptionMethods + "\nSelected Devices: " + selectedSerials,
                    "Server Configuration",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Frame Settings
        frame.setTitle("Create Server");
        frame.setSize(700, 800);
        frame.setVisible(true);
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
                "(if there is anything in there, it will assume all other addresses on the blacklist)",
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
                String whitelist = whitelistField.getText();
                String blacklist = blacklistField.getText();
                String rateLimit = rateLimitField.getText();

                // Validate input and process
                try {
                    int rateLimitValue = Integer.parseInt(rateLimit);

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
	
	public static void createServer1(JFrame frame) {
        // Set the layout
        frame.getContentPane().removeAll();
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

        // Join Password
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("Join Password:");
        frame.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField();
        frame.add(passwordField, gbc);

        // Info Label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("(you may leave this blank if you want to)", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        frame.add(infoLabel, gbc);

        // Server File Name
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel fileNameLabel = new JLabel("Server File Name:");
        frame.add(fileNameLabel, gbc);

        gbc.gridx = 1;
        JTextField fileNameField = new JTextField();
        frame.add(fileNameField, gbc);

        // Server File Directory
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel fileDirectoryLabel = new JLabel("Server File Directory:");
        frame.add(fileDirectoryLabel, gbc);

        gbc.gridx = 1;
        JPanel directoryPanel = new JPanel(new BorderLayout());
        JTextField directoryField = new JTextField("Default");
        JButton browseButton = new JButton(new ImageIcon("path/to/icon.png")); // Add your icon path
        browseButton.setToolTipText("Browse Directory");

        directoryPanel.add(directoryField, BorderLayout.CENTER);
        directoryPanel.add(browseButton, BorderLayout.EAST);
        frame.add(directoryPanel, gbc);

        // Continue Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton continueButton = new JButton("Continue");
        frame.add(continueButton, gbc);

        // Add ActionListener for Browse Button
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    directoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        // Add ActionListener for Continue Button
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                String fileName = fileNameField.getText();
                String directory = directoryField.getText();

                // Validate input and process
                if (fileName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a server file name.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    createServer2(frame);
                }
            }
        });

        // Set the frame properties
        frame.setTitle("Create Server");
        frame.setSize(400, 400);
    }
}