import javax.swing.*;
import java.awt.*;                          
import java.awt.event.*;  
import java.util.*; 
import java.util.List; 
import java.util.ArrayList; 
import java.util.stream.Collectors;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;          
import java.net.URL;
import java.util.Base64;   
import java.io.File;            
import java.io.FileWriter;       
import java.io.FileOutputStream; 
import java.nio.file.Files;    
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import java.time.*;
import java.time.format.*; 
import java.time.temporal.ChronoUnit;

public class CreateAccount {
	
	//Variables for Account Creation
    private boolean strictMode = false;
    private List<String> hashedSerials = new ArrayList<>();
    
    public List<String> getHashedSerials(){
    	return hashedSerials;
    }
    
    public String username = "";
    public String hashedCommunicationKey = "";
    
    //Get image
    public URL fileButtonIconURL = getClass().getResource("/images/File.png");
     
    //Access the Hash instance
    static Hash hash = CentralManager.getHash();
    
    //Access the App instance
    static App app = CentralManager.getApp();
    
    public void setupSuccess(JFrame frame, List<String> selected) {
		// Clear frame
		frame.getContentPane().removeAll();
        // Create the main panel to hold all components
	    frame.setSize(600, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title label
        JLabel titleLabel = new JLabel("Account Setup Successful!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        // Add a gap
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add success message
        JLabel successMessage = new JLabel("Your account has now been fully set up!");
        successMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        successMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(successMessage);

        // Add another gap
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add summary label
        JLabel summaryLabel = new JLabel("Summary of Encryption Process to unlock your account fully:");
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(summaryLabel);

        // Add the list (COMMENTED FOR PROGRAM TO WORK <REQUIRES LIST OF ENCRYPTION METHODS WHICH I DON'T HAVE>)
        /*DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < selected.size(); i++) {
            String item = selected.get(i);
            listModel.addElement((i + 1) + " " + item);
        }

        JList<String> list = new JList<>(listModel);
        list.setFont(new Font("Arial", Font.PLAIN, 14));
        list.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(list);
		*/
        // Add another gap
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add Continue button
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Arial", Font.PLAIN, 14));
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(e -> {
        	app.loggedInMenu(frame, username, "GPG Key Needed");
        });
        panel.add(continueButton);

        // Add panel to frame
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

public void createPassword(JFrame frame) {
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

    // Username Label and Field
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    centerPanel.add(usernameLabel, gbc);

    JTextField usernameField = new JTextField(20);
    usernameField.setMaximumSize(new Dimension(300, 25));
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridwidth = 3;
    centerPanel.add(usernameField, gbc);

    // Add a gap
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 5;
    centerPanel.add(Box.createVerticalStrut(10), gbc);

    // Description Label and TextArea
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    JLabel descriptionLabel2 = new JLabel("Description (Optional):");
    descriptionLabel2.setFont(new Font("SansSerif", Font.PLAIN, 14));
    centerPanel.add(descriptionLabel2, gbc);

    JTextArea descriptionArea = new JTextArea(5, 20);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.gridwidth = 3;
    centerPanel.add(descriptionArea, gbc);

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
    footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));  // Use FlowLayout.CENTER for horizontal centering
    footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Increased padding

    // Back Button
    JButton backButton = new JButton("Back");
    backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
    backButton.addActionListener(e -> {
        selectSecurityModules(frame);  // Call selectSecurityModules() when Back button is clicked
    });

    // Continue Button
    JButton continueButton = new JButton("Continue");
    continueButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
    continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // Ensuring button is centered
    continueButton.addActionListener(e -> {
        char[] pass = passwordField.getPassword();
        String passString = new String(pass);

        // Hash the password using SHA-256
        byte[] passwordHash = hash.hashSHA256(passString);

        username = usernameField.getText();

        // Generate the random communication key
        String communicationKey = generateRandomKey();

        String description = descriptionArea.getText();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (pass.length == 0) {
            JOptionPane.showMessageDialog(frame, "Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (orderListModel.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Select at least one encryption method!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Get the encryption order
            ArrayList<String> encryptionOrder = new ArrayList<>();
            for (int i = 0; i < orderListModel.size(); i++) {
                encryptionOrder.add(orderListModel.getElementAt(i));
            }

            try {
                // Format the content to include username and the hashed communication key
                String content = username + ":" + description + ":" + communicationKey;

                // Create the file with the content
                File keyFile = new File(System.getProperty("user.home") + File.separator + "communication_key.txt");
                try (FileWriter writer = new FileWriter(keyFile)) {
                    writer.write(content);
                }

                // Encrypt the file using the password hash (byte array)
                byte[] encryptedData = encryptFileWithOrder(keyFile, passwordHash, encryptionOrder);

                // Save the encrypted file
                File encryptedFile = new File(System.getProperty("user.home") + File.separator + "communication_key_encrypted.txt");
                try (FileOutputStream fos = new FileOutputStream(encryptedFile)) {
                    fos.write(encryptedData);
                }

                JOptionPane.showMessageDialog(frame, "File created and encrypted successfully:\n" + encryptedFile.getAbsolutePath(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                setupSuccess(frame, null); // Proceed to the next step
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
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

//Generate Random Key and Hash
private String generateRandomKey() {
 SecureRandom random = new SecureRandom();
 byte[] keyBytes = new byte[512]; // 4096 bits = 512 bytes
 random.nextBytes(keyBytes);
 String key = Base64.getEncoder().encodeToString(keyBytes); // UTF-8 compatible string
 return hash.hashSHA512(key); // Hash the key using SHA-512
}
private byte[] encryptFileWithOrder(File file, byte[] password, ArrayList<String> encryptionOrder) throws Exception {
    // Read the file's content
    byte[] fileContent = Files.readAllBytes(file.toPath());

    // Apply encryption for each algorithm in the order
    byte[] encryptedData = fileContent;
    for (String algorithm : encryptionOrder) {
        switch (algorithm) {
            case "AES":
                encryptedData = encryptWithAES(encryptedData, password);
                break;
            case "Serpent":
                encryptedData = encryptWithSerpent(encryptedData, password);
                break;
            case "Twofish":
                encryptedData = encryptWithTwofish(encryptedData, password);
                break;
            case "Camellia":
                encryptedData = encryptWithCamellia(encryptedData, password);
                break;
            case "Kuznyechik":
                encryptedData = encryptWithKuznyechik(encryptedData, password);
                break;
            default:
                throw new IllegalArgumentException("Unknown encryption algorithm: " + algorithm);
        }
    }

    return encryptedData;
}

// AES Encryption
private byte[] encryptWithAES(byte[] data, byte[] password) throws Exception {
    SecretKeySpec keySpec = new SecretKeySpec(password, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    return cipher.doFinal(data);
}

// Serpent Encryption (Example with BouncyCastle)
private byte[] encryptWithSerpent(byte[] data, byte[] password) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    Cipher cipher = Cipher.getInstance("Serpent", "BC");
    SecretKeySpec keySpec = new SecretKeySpec(password, "Serpent");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    return cipher.doFinal(data);
}

// Twofish Encryption (Example with BouncyCastle)
private byte[] encryptWithTwofish(byte[] data, byte[] password) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    Cipher cipher = Cipher.getInstance("Twofish", "BC");
    SecretKeySpec keySpec = new SecretKeySpec(password, "Twofish");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    return cipher.doFinal(data);
}

// Camellia Encryption (Example with BouncyCastle)
private byte[] encryptWithCamellia(byte[] data, byte[] password) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    Cipher cipher = Cipher.getInstance("Camellia", "BC");
    SecretKeySpec keySpec = new SecretKeySpec(password, "Camellia");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    return cipher.doFinal(data);
}

// Kuznyechik Encryption (Example with BouncyCastle)
private byte[] encryptWithKuznyechik(byte[] data, byte[] password) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    Cipher cipher = Cipher.getInstance("GOST3412-2015", "BC");
    SecretKeySpec keySpec = new SecretKeySpec(password, "GOST3412-2015");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    return cipher.doFinal(data);
}

private void updateDeviceList(JPanel createAccountPanel) {
// Get the available storage devices based on the mode selected
List<String> availableDevices = getAvailableStorageDevices();

// Find the devicePanel in the createAccountPanel and update it
JPanel devicePanel = null;
Component[] components = createAccountPanel.getComponents();

// Look for the existing device panel
for (Component component : components) {
    if (component instanceof JPanel && ((JPanel) component).getLayout() instanceof GridLayout) {
        devicePanel = (JPanel) component;
        break; // Found the existing device panel
    }
}

// If no devicePanel found, create a new one
if (devicePanel == null) {
    devicePanel = new JPanel();
    devicePanel.setLayout(new GridLayout(0, 1, 10, 10)); // Use a grid layout
    createAccountPanel.add(devicePanel);
} else {
    devicePanel.removeAll();  // Clear previous buttons
}

// Create toggle buttons for the available devices
for (String device : availableDevices) {
    JToggleButton deviceToggleButton = new JToggleButton(device);
    deviceToggleButton.setToolTipText("Click to select " + device);
    devicePanel.add(deviceToggleButton);  // Add the toggle button
}

// Refresh the devicePanel
createAccountPanel.revalidate();
createAccountPanel.repaint();
}

public JPanel modulePanel; // Holds each individual module
public JPanel moduleListPanel; // Holds the list of module panels
public int MODULES_PER_PAGE = 3; // Number of modules to display per page
public int currentPage = 1; // Tracks the current page
public List<String> selectedSecurityMethods = new ArrayList<>(); // Shows the path of how to decrypt account

public void selectSecurityModules(JFrame frame) {
frame.getContentPane().removeAll();
frame.setSize(600, 550);
frame.setLayout(new BorderLayout());

// Header Panel
JPanel headerPanel = new JPanel();
headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Add margin around the header panel

JLabel headerLabel = new JLabel("Account Protection Modules", SwingConstants.CENTER);
headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
headerPanel.add(headerLabel);

headerPanel.add(Box.createVerticalStrut(10));

// Description panel
JLabel descriptionLabel = new JLabel(
        "<html>If you have modules imported in the module folder, you may load them to further secure your account. If not, you may continue.</html>",
        SwingConstants.CENTER);
descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
headerPanel.add(descriptionLabel);

frame.add(headerPanel, BorderLayout.NORTH);

// Module List Panel
moduleListPanel = new JPanel();
moduleListPanel.setLayout(new GridLayout(MODULES_PER_PAGE, 1, 10, 10));
moduleListPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Add margin around the module panel
JScrollPane moduleScrollPane = new JScrollPane(moduleListPanel);
moduleScrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove scrollpane border for cleaner look
frame.add(moduleScrollPane, BorderLayout.CENTER);

// Footer Panel with Navigation Buttons
JPanel footerPanel = new JPanel();
footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Add margin around the footer panel

JPanel navigationPanel = new JPanel();
JButton previousButton = new JButton("PREVIOUS");
JButton nextButton = new JButton("NEXT");
JLabel pageLabel = new JLabel("PAGE 1/2", SwingConstants.CENTER);
pageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

previousButton.addActionListener(e -> navigateModules(-1, pageLabel));
nextButton.addActionListener(e -> navigateModules(1, pageLabel));

navigationPanel.add(previousButton);
navigationPanel.add(pageLabel);
navigationPanel.add(nextButton);

JButton continueButton = new JButton("Continue");
continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
continueButton.addActionListener(e -> createPassword(frame));

JButton backButton = new JButton("Back");
backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
backButton.addActionListener(e -> secondGPGIdentity(frame)); // Back button action listener

footerPanel.add(navigationPanel);
footerPanel.add(Box.createVerticalStrut(10));
footerPanel.add(continueButton); // Add Continue button first
footerPanel.add(Box.createVerticalStrut(5));
footerPanel.add(backButton); // Add Back button below Continue
frame.add(footerPanel, BorderLayout.SOUTH);

// Load Initial Modules
loadModules();
}

private void navigateModules(int direction, JLabel pageLabel) {
int totalPages = 2; // Example total pages
currentPage += direction;

if (currentPage < 1) {
    currentPage = 1;
} else if (currentPage > totalPages) {
    currentPage = totalPages;
}

pageLabel.setText("PAGE " + currentPage + "/" + totalPages);
loadModules();
}

private void loadModules() {
moduleListPanel.removeAll();

// Folder containing module files
File moduleFolder = new File("src/main/resources/modules");
if (!moduleFolder.exists() || !moduleFolder.isDirectory()) {
    JLabel errorLabel = new JLabel("No modules folder found.");
    moduleListPanel.add(errorLabel);
    moduleListPanel.revalidate();
    moduleListPanel.repaint();
    return;
}

moduleListPanel.revalidate();
moduleListPanel.repaint();
}

public void secondGPGIdentity(JFrame frame) {
SwingUtilities.invokeLater(() -> {
    frame.setTitle("AmnesicChat - Create GPG Identity");
    frame.setSize(700, 450);
    frame.getContentPane().removeAll();
    frame.setLayout(new BorderLayout());

    // Main panel setup
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
    frame.add(mainPanel, BorderLayout.CENTER);

    // Header
    JLabel headerLabel = new JLabel("Create GPG Identity", SwingConstants.CENTER);
    headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
    headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(headerLabel);

    mainPanel.add(Box.createVerticalStrut(10));

    JLabel subHeaderLabel = new JLabel(
            "<html>It is recommended to use a pseudo identity. Do not use your real identity unless necessary.<br>Hover over the text boxes and tooltip for more.</html>",
            SwingConstants.CENTER);
    subHeaderLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    subHeaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(subHeaderLabel);

    mainPanel.add(Box.createVerticalStrut(20));

    // Form panel
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

 // Algorithm field
    gbc.gridx = 0;
    gbc.gridy = 0;
    frame.add(mainPanel, BorderLayout.CENTER);
    JLabel algorithmLabel = new JLabel("Algorithm:");
    algorithmLabel.setToolTipText("Select the encryption algorithm (e.g., RSA, ECC, DSA).");
    formPanel.add(algorithmLabel, gbc);
    gbc.gridx = 1;
    String[] algorithms = {"RSA", "ECC", "DSA"};
    JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);
    algorithmComboBox.setSelectedIndex(-1); // No default selection
    formPanel.add(algorithmComboBox, gbc);

 // Key size field
    gbc.gridx = 0;
    gbc.gridy = 1;
    JLabel keySizeLabel = new JLabel("Key Size:");
    keySizeLabel.setToolTipText("Select the key size (e.g., 2048, 4096).");
    formPanel.add(keySizeLabel, gbc);
    gbc.gridx = 1;
    JComboBox<String> keySizeComboBox = new JComboBox<>();
    formPanel.add(keySizeComboBox, gbc);

 // Comments field
    gbc.gridx = 0;
    gbc.gridy = 2;
    JLabel commentsLabel = new JLabel("Comments:");
    commentsLabel.setToolTipText("Optional comments for the GPG key.");
    formPanel.add(commentsLabel, gbc);
    gbc.gridx = 1;
    JTextField commentsField = new JTextField(20);
    formPanel.add(commentsField, gbc);
    
 // Export keys field
    gbc.gridx = 0;
    gbc.gridy = 3;
    JLabel exportKeysLabel = new JLabel("Export Keys?");
    exportKeysLabel.setToolTipText("Choose whether to export the keys (Both, Secret Only, Public Only, None).");
    formPanel.add(exportKeysLabel, gbc);
    gbc.gridx = 1;
    String[] exportOptions = {"BOTH", "SECRET ONLY", "PUBLIC ONLY", "NONE"};
    JComboBox<String> exportKeysComboBox = new JComboBox<>(exportOptions);
    exportKeysComboBox.setSelectedIndex(-1); // No default selection
    formPanel.add(exportKeysComboBox, gbc);
    
    // Populate key size options dynamically
    algorithmComboBox.addActionListener(e -> {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        keySizeComboBox.removeAllItems();
        if ("RSA".equals(selectedAlgorithm)) {
            keySizeComboBox.addItem("2048");
            keySizeComboBox.addItem("3072");
            keySizeComboBox.addItem("4096");
        } else if ("ECC".equals(selectedAlgorithm)) {
            keySizeComboBox.addItem("256");
            keySizeComboBox.addItem("384");
            keySizeComboBox.addItem("521");
        } else if ("DSA".equals(selectedAlgorithm)) {
            keySizeComboBox.addItem("1024");
            keySizeComboBox.addItem("2048");
            keySizeComboBox.addItem("3072");
        }
        keySizeComboBox.setSelectedIndex(-1); // Reset selection
    });

    mainPanel.add(formPanel);

    // Continue button
    JButton continueButton = new JButton("Generate GPG Key");
    continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    continueButton.addActionListener(e -> {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        String keySize = (String) keySizeComboBox.getSelectedItem();
        if (algorithm == null || keySize == null) {
            JOptionPane.showMessageDialog(frame, "Please select both algorithm and key size.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                // Generate GPG key
                String name = "User Name"; // Replace with actual input
                String email = "user@example.com"; // Replace with actual input
                String passphrase = "securepass"; // Replace with actual passphrase

                //String privateKey = generateGPGKey(name, email, passphrase, algorithm, Integer.parseInt(keySize));

                // Save private key to a file
                File file = new File("privateKey.asc");
                try (FileOutputStream out = new FileOutputStream(file)) {
                    //out.write(privateKey.getBytes());
                }

                JOptionPane.showMessageDialog(frame, "GPG Key successfully created.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                selectSecurityModules(frame);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error while generating GPG key: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    
 // Back button
    JButton backButton = new JButton("Back");
    backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    backButton.addActionListener(e -> createGPGIdentity(frame));
    
    
    mainPanel.add(continueButton);
    mainPanel.add(Box.createVerticalStrut(10));
    mainPanel.add(backButton);

    frame.add(mainPanel);
    frame.revalidate();
    frame.repaint();
});
}

public void createGPGIdentity(JFrame frame) {
SwingUtilities.invokeLater(() -> {
    frame.setTitle("AmnesicChat - Create GPG Identity");
    frame.setSize(700, 450);
    frame.getContentPane().removeAll();
    frame.setLayout(new BorderLayout());

    // Main panel setup
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
    frame.add(mainPanel, BorderLayout.CENTER);

    // Header
    JLabel headerLabel = new JLabel("Create GPG Identity", SwingConstants.CENTER);
    headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
    headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(headerLabel);

    mainPanel.add(Box.createVerticalStrut(10));

    JLabel subHeaderLabel = new JLabel(
            "<html>It is recommended to use a pseudo identity. Do not use your real identity unless necessary.<br>" +
                    "Hover over the text boxes and tooltip for more.</html>",
            SwingConstants.CENTER);
    subHeaderLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    subHeaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(subHeaderLabel);

    mainPanel.add(Box.createVerticalStrut(20));

    // Form panel
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Name field
    gbc.gridx = 0;
    gbc.gridy = 0;
    JLabel nameLabel = new JLabel("Name:");
    nameLabel.setToolTipText("Enter your preferred name. Must be at least 4 characters.");
    formPanel.add(nameLabel, gbc);
    gbc.gridx = 1;
    JTextField nameField = new JTextField(20);
    nameField.setToolTipText("Example: John Doe. Use a pseudonym for privacy.");
    formPanel.add(nameField, gbc);

    // Email field
    gbc.gridx = 0;
    gbc.gridy = 1;
    JLabel emailLabel = new JLabel("E-mail:");
    emailLabel.setToolTipText("Enter your email address for the GPG identity.");
    formPanel.add(emailLabel, gbc);
    gbc.gridx = 1;
    JTextField emailField = new JTextField(20);
    emailField.setToolTipText("Example: user@example.com");
    formPanel.add(emailField, gbc);

    // Password field
    gbc.gridx = 0;
    gbc.gridy = 2;
    JLabel passwordLabel = new JLabel("Passphrase:");
    passwordLabel.setToolTipText("Set a strong password for your GPG key. Optional but recommended.");
    formPanel.add(passwordLabel, gbc);
    gbc.gridx = 1;
    JPasswordField passphraseField = new JPasswordField(20);
    passphraseField.setToolTipText("Leave blank to skip. A strong password has 8+ characters.");
    formPanel.add(passphraseField, gbc);

    // Expiry field
    gbc.gridx = 0;
    gbc.gridy = 3;
    JLabel expiryLabel = new JLabel("Expiry:");
    expiryLabel.setToolTipText("Set the expiry date for the GPG key in the format DD-MM-YYYY.");
    formPanel.add(expiryLabel, gbc);
    gbc.gridx = 1;
    JTextField expiryField = new JTextField(20);
    expiryField.setText("DD-MM-YYYY");
    expiryField.setToolTipText("Example: 31-12-2025. Expiry must be at least 90 days in the future.");
    formPanel.add(expiryField, gbc);

    // Info button
    gbc.gridx = 2;
    JButton infoButton = new JButton("i");
    infoButton.setToolTipText("Your GPG key will expire on the specified date. You must generate a new key to continue using encryption.");
    formPanel.add(infoButton, gbc);

    mainPanel.add(formPanel);

    // Warning panel
    JPanel warningPanel = new JPanel();
    warningPanel.setLayout(new BoxLayout(warningPanel, BoxLayout.Y_AXIS));
    warningPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    mainPanel.add(warningPanel);

    mainPanel.add(Box.createVerticalStrut(20));

    // Button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
    mainPanel.add(buttonPanel);

    // Back button
    JButton backButton = new JButton("Back");
    backButton.addActionListener(e -> insertGPGIdentity(frame));
    buttonPanel.add(backButton);

    // Continue button
    JButton continueButton = new JButton("Continue");
    continueButton.addActionListener(e -> {
        boolean doVerify = false;

        if (doVerify) {
            warningPanel.removeAll(); // Clear previous warnings
            boolean hasErrors = false;
            boolean hasWarnings = false;
            StringBuilder warnings = new StringBuilder();

            // Validate name
            String name = nameField.getText().trim();
            if (name.length() < 4) {
                JLabel errorLabel = new JLabel("Name must have at least 4 characters.");
                errorLabel.setForeground(Color.RED);
                warningPanel.add(errorLabel);
                hasErrors = true;
            }

            // Validate email
            String email = emailField.getText().trim();
            if (!email.matches(".+@.+")) {
                JLabel errorLabel = new JLabel("Email must be valid (e.g., name@example.com).");
                errorLabel.setForeground(Color.RED);
                warningPanel.add(errorLabel);
                hasErrors = true;
            }

            // Validate passphrase
            char[] password = passphraseField.getPassword();
            if (password.length == 0) {
                warnings.append("- No password set. This is not recommended for security.\n");
                hasWarnings = true;
            } else if (password.length < 8) {
                warnings.append("- Password is weak (less than 8 characters).\n");
                hasWarnings = true;
            }

            // Validate expiry
            String expiry = expiryField.getText().trim();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate expiryDate = LocalDate.parse(expiry, formatter);
                LocalDate today = LocalDate.now();
                long daysBetween = ChronoUnit.DAYS.between(today, expiryDate);

                if (!expiryDate.isAfter(today)) {
                    JLabel errorLabel = new JLabel("Expiry date must be in the future.");
                    errorLabel.setForeground(Color.RED);
                    warningPanel.add(errorLabel);
                    hasErrors = true;
                } else if (daysBetween < 90) {
                    warnings.append("- Expiry date is less than 90 days from now.\n");
                    hasWarnings = true;
                }
            } catch (DateTimeParseException ex) {
                JLabel errorLabel = new JLabel("Expiry date must be valid and in the format DD-MM-YYYY.");
                errorLabel.setForeground(Color.RED);
                warningPanel.add(errorLabel);
                hasErrors = true;
            }

            warningPanel.revalidate();
            warningPanel.repaint();

            // Handle results
            if (hasErrors) {
                JOptionPane.showMessageDialog(frame, "Please correct the highlighted errors.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (hasWarnings) {
                // Show warnings popup
                int choice = JOptionPane.showOptionDialog(frame,
                        "Warnings:\n" + warnings.toString(),
                        "Warnings",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new String[]{"Back", "Continue"},
                        "Back");
                if (choice == JOptionPane.NO_OPTION) {
                    secondGPGIdentity(frame);
                }
            } else {
                secondGPGIdentity(frame);
            }
        } else {
            secondGPGIdentity(frame);
        }
    });
    buttonPanel.add(continueButton);

    frame.add(mainPanel);
    frame.revalidate();
    frame.repaint();
});
}

public static void loadGPGKey(JFrame frame, File gpgKeyFile) {
frame.getContentPane().removeAll();

// TO FIX
// Set the title and layout of the frame
frame.setTitle("GPG Identity Loaded");
frame.setLayout(new BorderLayout());

// Create a panel for the content
JPanel contentPanel = new JPanel();
contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

// Add the GPG Identity Loaded header
JLabel headerLabel = new JLabel("GPG Identity Loaded", SwingConstants.CENTER);
headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
contentPanel.add(headerLabel);

contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacer

// Parse the GPG key file to extract details
String name = "Unknown";
String email = "Unknown";
String expiry = "Unknown";
String algorithm = "Unknown";
String comments = "Unknown";
String fingerprint = "Unknown";

if (gpgKeyFile != null && gpgKeyFile.exists()) {
try (BufferedReader reader = new BufferedReader(new FileReader(gpgKeyFile))) {
    String line;
    while ((line = reader.readLine()) != null) {
        if (line.startsWith("Name:")) {
        
        } else if (line.startsWith("E-mail:")) {
          
        } else if (line.startsWith("Expiry:")) {

        } else if (line.startsWith("Algorithm:")) {
          
        } else if (line.startsWith("Comments:")) {
         
        } else if (line.startsWith("Fingerprint:")) {
           
        }
    }
} catch (IOException e) {
    JOptionPane.showMessageDialog(frame, "Failed to read GPG key file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}
} else {
JOptionPane.showMessageDialog(frame, "GPG key file not found.", "Error", JOptionPane.ERROR_MESSAGE);
}

// Add information labels
String[] labels = {
"Name: " + name,
"E-mail: " + email,
"Expiry: " + expiry,
"Algorithm: " + algorithm,
"Comments: " + comments,
"Fingerprint: " + fingerprint
};

for (String text : labels) {
JLabel label = new JLabel(text);
label.setFont(new Font("Arial", Font.PLAIN, 14));
label.setAlignmentX(Component.CENTER_ALIGNMENT);
contentPanel.add(label);
contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
}

// Add the buttons panel
JPanel buttonPanel = new JPanel();
buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

JButton returnButton = new JButton("Return");
JButton continueButton = new JButton("Continue");

buttonPanel.add(returnButton);
buttonPanel.add(continueButton);

// Add components to the frame
frame.add(contentPanel, BorderLayout.CENTER);
frame.add(buttonPanel, BorderLayout.SOUTH);
frame.revalidate();
frame.repaint();
}
    
    public void insertGPGIdentity(JFrame frame) {
	    SwingUtilities.invokeLater(() -> {
	        frame.getContentPane().removeAll();
	        frame.setTitle("AmnesicChat - GPG Identity");
	        frame.setSize(650, 350);

	        // Main panel setup
	        JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

	        // Header label
	        JLabel headerLabel = new JLabel("Create GPG Identity", SwingConstants.CENTER);
	        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
	        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        mainPanel.add(headerLabel);

	        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing

	        // Instruction label
	        JLabel instructionLabel = new JLabel("Would you like to import your own GPG key?");
	        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
	        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        mainPanel.add(instructionLabel);

	        JLabel instructionSubLabel = new JLabel(
	                "If yes, please locate the private key using the directory finder below.",
	                SwingConstants.CENTER);
	        instructionSubLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
	        instructionSubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        mainPanel.add(instructionSubLabel);

	        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing

	        // File chooser panel
	        JPanel fileChooserPanel = new JPanel();
	        fileChooserPanel.setLayout(new BoxLayout(fileChooserPanel, BoxLayout.X_AXIS));

	        JTextField filePathField = new JTextField();
	        JButton loadKeyButton = new JButton("..."); // Initially "..." for browsing
	        loadKeyButton.setPreferredSize(new Dimension(80, 30));

	        // Load the icon for the "..." button
	        if (fileButtonIconURL != null) {
	        	 ImageIcon originalIcon = new ImageIcon(fileButtonIconURL);
                 Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Resize image to 50x50
                 ImageIcon resizedIcon = new ImageIcon(scaledImage);
	        	
	            loadKeyButton.setIcon(resizedIcon);
	            loadKeyButton.setText(""); // Clear text if icon is set
	        }

	        filePathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, loadKeyButton.getPreferredSize().height));

	        fileChooserPanel.add(filePathField);
	        fileChooserPanel.add(Box.createHorizontalStrut(10)); // Add spacing
	        fileChooserPanel.add(loadKeyButton);

	     // Create button
	        JButton createButton = new JButton("Create my own key");
	        createButton.setPreferredSize(new Dimension(200, 40));
	        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	        createButton.addActionListener(e -> {
	            String filePath = filePathField.getText();
	            if (filePath.isEmpty()) {
	                createGPGIdentity(frame); // Create a new GPG identity
	            } else {
	                if (validateGPGKey(filePath)) {
	                    loadGPGKey(frame, new File(filePath)); // Pass the file path as a File object
	                } else {
	                    JOptionPane.showMessageDialog(frame, "Invalid GPG key! Please select a valid secret key.",
	                            "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        });
	        
	        // Change button text to "Load Key" when typing in the text field
	        filePathField.getDocument().addDocumentListener(new DocumentListener() {
	            @Override
	            public void insertUpdate(DocumentEvent e) {
	                updateButtonText();
	            }

	            @Override
	            public void removeUpdate(DocumentEvent e) {
	                updateButtonText();
	            }

	            @Override
	            public void changedUpdate(DocumentEvent e) {
	                updateButtonText();
	            }

	            private void updateButtonText() {
	                createButton.setText(filePathField.getText().isEmpty() ? "Create my own key" : "Load Key");
	            }
	        });

	        // File chooser action listener
	        loadKeyButton.addActionListener(e -> {
	            String filePath = filePathField.getText();
	            if (filePath.isEmpty()) {
	                JFileChooser fileChooser = new JFileChooser();
	                fileChooser.setFileFilter(new FileNameExtensionFilter("PGP/GPG Files", "asc")); // Restrict to .asc files
	                int result = fileChooser.showOpenDialog(frame);
	                if (result == JFileChooser.APPROVE_OPTION) {
	                    filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
	                }
	            } else {
	                if (validateGPGKey(filePath)) {
	                    loadGPGKey(frame, new File(filePath)); // Pass the file path as a File object
	                } else {
	                    JOptionPane.showMessageDialog(frame, "Invalid GPG key! Please select a valid secret key.",
	                            "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        });

	        mainPanel.add(fileChooserPanel);

	        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
	        
	        mainPanel.add(createButton);

	        // Back button
	        JButton backButton = new JButton("Back");
	        backButton.setPreferredSize(new Dimension(200, 40));
	        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	        backButton.addActionListener(e -> createAccount(frame));
	        mainPanel.add(Box.createVerticalStrut(10)); // Add spacing between buttons
	        mainPanel.add(backButton);

	        frame.add(mainPanel);
	        frame.revalidate();
	        frame.repaint();
	    });
	}

	public boolean validateGPGKey(String filePath) {
	    return true;
	}

	public List<String> getAvailableStorageDevices() {
	    List<String> devices = new ArrayList<>();

	        // Use hardware information (SystemInfo) to get devices
	        SystemInfo systemInfo = new SystemInfo();
	        List<HWDiskStore> diskStores = systemInfo.getHardware().getDiskStores();

	        for (HWDiskStore diskStore : diskStores) {
	            devices.add(diskStore.getName());
	        } 	   
	    return devices;
	}
    
    public void createAccount(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle("AmnesicChat - Create Account");
            frame.setSize(650, 450);

            // Fetch available storage devices using OSHI
            List<String> deviceNames = getStorageDeviceNames();

            // Mock serial numbers for each device
            List<String> serialNumbers = deviceNames.stream()
                    .map(device -> "Serial-" + device.hashCode())
                    .collect(Collectors.toList());

            // A map to associate disk names with their serial numbers
            Map<String, String> diskToSerialMap = new HashMap<>();
            for (int i = 0; i < deviceNames.size(); i++) {
                diskToSerialMap.put(deviceNames.get(i), serialNumbers.get(i));
            }

            frame.getContentPane().removeAll();

            // Main panel
            JPanel createAccountPanel = new JPanel();
            createAccountPanel.setLayout(new BoxLayout(createAccountPanel, BoxLayout.Y_AXIS));
            createAccountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Header and instruction labels
            JLabel headerLabel = new JLabel("Create Device Lock", SwingConstants.CENTER);
            headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            createAccountPanel.add(headerLabel);

            createAccountPanel.add(Box.createVerticalStrut(10)); // Spacing
            
            // Instruction label
            JLabel instructionLabel = new JLabel(
            		"<html>Choose which storage devices you want to use as verification.<br>"
                            + "The storage devices selected are required to unlock your account.<br>"
                            + "It is not recommended to use your USB as the only device lock.</html>",
                    SwingConstants.CENTER);
            instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            createAccountPanel.add(instructionLabel);
            createAccountPanel.add(Box.createVerticalStrut(10));
            
            // Warning message
            JLabel warningLabel = new JLabel(
                    "<html><i>STRICT MODE IS NOT RECOMMENDED.<br>"
                            + "IF THE DEVICE(S) YOU ASSIGN GET DAMAGED OR LOST,<br>"
                            + "THE ACCOUNT WILL NEVER BE RECOVERED EVER.</i></html>",
                    SwingConstants.CENTER);
            warningLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            warningLabel.setForeground(Color.RED);
            warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            createAccountPanel.add(Box.createVerticalStrut(10)); // Add spacing
            createAccountPanel.add(warningLabel);
            createAccountPanel.add(Box.createVerticalStrut(20)); // Add spacing
            
            // Strict mode panel
            JPanel strictModePanel = new JPanel();
            strictModePanel.setLayout(new BoxLayout(strictModePanel, BoxLayout.X_AXIS));
            strictModePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel strictModeLabel = new JLabel("Strict Mode: ");
            strictModeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            strictModePanel.add(strictModeLabel);
            JRadioButton yesButton = new JRadioButton("YES");
            JRadioButton noButton = new JRadioButton("NO", true); // Default to "NO"
            ButtonGroup strictModeGroup = new ButtonGroup();
            strictModeGroup.add(yesButton);
            strictModeGroup.add(noButton);
            // Add tooltips on hover to explain what strict mode does
            yesButton.setToolTipText("Enables Strict Mode, which locks your account to specific devices. If the devices are lost or damaged, the account cannot be recovered.");
            noButton.setToolTipText("Disables Strict Mode. This allows recovery of your account if you lose access to the specific devices.");
            
         // Add action listeners to update the strictMode variable
            yesButton.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    strictMode = true; // Set strictMode to true when "YES" is selected
                }
            });

            noButton.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    strictMode = false; // Set strictMode to false when "NO" is selected
                }
            });
            
            strictModePanel.add(yesButton);
            strictModePanel.add(Box.createHorizontalStrut(10)); // Add spacing
            strictModePanel.add(noButton);
            createAccountPanel.add(strictModePanel);
            
            // Panel for device toggles
            JPanel devicePanel = new JPanel();
            devicePanel.setLayout(new GridLayout(0, 1, 10, 10));  // Grid layout for device toggles

            // List to keep track of selected serial numbers
            List<String> selectedSerials = new ArrayList<>();

            // Displaying each storage device on the UI
            for (String deviceName : deviceNames) {
                JToggleButton deviceToggleButton = new JToggleButton(deviceName);

                // Add action listener to toggle button
                deviceToggleButton.addActionListener(e -> {
                    if (deviceToggleButton.isSelected()) {
                        // Add the corresponding serial number to the list
                        selectedSerials.add(diskToSerialMap.get(deviceName));
                    } else {
                        // Remove the serial number from the list if deselected
                        selectedSerials.remove(diskToSerialMap.get(deviceName));
                    }
                });

                devicePanel.add(deviceToggleButton);
            }

            createAccountPanel.add(devicePanel);
            createAccountPanel.add(Box.createVerticalStrut(20)); // Spacing

            // Continue button
            JButton continueButton = new JButton("Continue");
            continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            continueButton.addActionListener(e -> {
                // Hash the serial numbers, sort them, and pass them to the next function
                hashedSerials = selectedSerials.stream()
                    .map(hash::hashSHA512)
                    .sorted()
                    .collect(Collectors.toList());

                // Pass the sorted hashed serials to the next function
                insertGPGIdentity(frame);
            });

            createAccountPanel.add(continueButton);
            createAccountPanel.add(Box.createVerticalStrut(10)); // Spacing

            // Back button to return to the main menu
            JButton backButton = new JButton("Back");
            backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            backButton.addActionListener(e -> app.mainMenu(frame));
            createAccountPanel.add(backButton);

            frame.getContentPane().add(createAccountPanel, BorderLayout.CENTER);

            frame.revalidate();
            frame.repaint();
        });
    }

    // Fetch storage device names using OSHI
    private List<String> getStorageDeviceNames() {
        List<String> deviceNames = new ArrayList<>();
        SystemInfo systemInfo = new SystemInfo();
        oshi.hardware.HardwareAbstractionLayer hardware = systemInfo.getHardware();
        List<HWDiskStore> diskStores = hardware.getDiskStores();

        // Iterate through each disk and add its name to the list, filtering out logical volumes
        for (HWDiskStore disk : diskStores) {
            String diskName = disk.getModel();  // Get the model of the disk (e.g., "Samsung 970 Evo")
            
            // Filter out logical volumes or devices with names that suggest they are not physical
            if (!isLogicalVolume(diskName)) {
                deviceNames.add(diskName);
            }
        }

        if (deviceNames.isEmpty()) {
            deviceNames.add("No devices found.");
        }

        return deviceNames;
    }

    // Function to check if the device name suggests it is a logical volume
    private boolean isLogicalVolume(String deviceName) {
        // Check if the device name contains typical logical volume keywords
        String[] logicalKeywords = {"logical", "volume", "raid", "virtual", "part", "mapper", "md"};
        for (String keyword : logicalKeywords) {
            if (deviceName.toLowerCase().contains(keyword)) {
                return true; // It's likely a logical volume
            }
        }
        return false; // Otherwise, assume it's a physical device
    }
}
