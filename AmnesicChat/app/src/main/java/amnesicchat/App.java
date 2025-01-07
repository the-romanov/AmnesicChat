import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.*;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;



public class App {

    private JFrame frame; //JFrame is private so that we can isolate the variable to prevent potential tampering.
    private boolean useHardwareInfoMode = true; // Flag to switch between modes
    public int baseWidth = 650;
    public int baseHeight = 350;	
    
    public App() {
        frame = new JFrame("AmnesicChat");
    }

    public JFrame getJFrame() {
        return frame;
    }

    	public List<String> getAvailableStorageDevices() {
    	    List<String> devices = new ArrayList<>();

    	    if (useHardwareInfoMode) {
    	        // Use hardware information (SystemInfo) to get devices
    	        SystemInfo systemInfo = new SystemInfo();
    	        List<HWDiskStore> diskStores = systemInfo.getHardware().getDiskStores();

    	        for (HWDiskStore diskStore : diskStores) {
    	            devices.add(diskStore.getName());
    	        }
    	    } else {
    	        // Use file system roots (drives)
    	        File[] roots = File.listRoots();
    	        for (File root : roots) {
    	            devices.add(root.getAbsolutePath());
    	        }
    	    }
    	    return devices;
    	}
    	
    	public void frameUpdate(JFrame frame, int aD) {
    		int aH = 0;
            if (aD != 0) {
                aH = aD * 50;
            }

            // Set the dynamic size
            frame.setSize(650, 350 + aH);
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

        public void selectSecurityModules(JFrame frame) {
            this.frame = frame; // Set the public frame variable for external access
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
            continueButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Continue clicked!"));

            footerPanel.add(navigationPanel);
            footerPanel.add(Box.createVerticalStrut(10));
            footerPanel.add(continueButton);
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

            // Get all files in the directory
            File[] moduleFiles = moduleFolder.listFiles((dir, name) -> name.endsWith(".json")); // Assuming JSON metadata
            if (moduleFiles == null || moduleFiles.length == 0) {
                JLabel noModulesLabel = new JLabel("No modules found.");
                moduleListPanel.add(noModulesLabel);
                moduleListPanel.revalidate();
                moduleListPanel.repaint();
                return;
            }

            // Process files for pagination
            int start = (currentPage - 1) * MODULES_PER_PAGE;
            int end = Math.min(start + MODULES_PER_PAGE, moduleFiles.length);

            for (int i = start; i < end; i++) {
                File moduleFile = moduleFiles[i];

                // Read metadata (JSON example)
                String moduleName = moduleFile.getName();
                String moduleDescription = "Description not available";
                try {
                    String jsonContent = Files.readString(moduleFile.toPath(), StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(jsonContent);
                    moduleName = jsonObject.optString("name", moduleName);
                    moduleDescription = jsonObject.optString("description", moduleDescription);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                // Create UI components
                JPanel modulePanel = new JPanel();
                modulePanel.setLayout(new BorderLayout());
                modulePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JLabel moduleTitle = new JLabel(moduleName);
                moduleTitle.setFont(new Font("SansSerif", Font.BOLD, 14));

                JLabel moduleDescriptionLabel = new JLabel(moduleDescription);
                moduleDescriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

                JCheckBox moduleCheckBox = new JCheckBox();
                moduleCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);

                modulePanel.add(moduleTitle, BorderLayout.NORTH);
                modulePanel.add(moduleDescriptionLabel, BorderLayout.CENTER);
                modulePanel.add(moduleCheckBox, BorderLayout.EAST);

                moduleListPanel.add(modulePanel);
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

    	        // Add the form panel to the main panel
    	        mainPanel.add(formPanel);

    	        // Warning panel
    	        JPanel warningPanel = new JPanel();
    	        warningPanel.setLayout(new BoxLayout(warningPanel, BoxLayout.Y_AXIS));
    	        warningPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	        mainPanel.add(warningPanel);

    	        mainPanel.add(Box.createVerticalStrut(20));

    	        // Add event listener for algorithm selection
    	        algorithmComboBox.addActionListener(e -> {
    	            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
    	            keySizeComboBox.removeAllItems();
    	            if ("RSA".equals(selectedAlgorithm)) {
    	                keySizeComboBox.addItem("2048");
    	                keySizeComboBox.addItem("3072");
    	                keySizeComboBox.addItem("4096");
    	            } else if ("ECC".equals(selectedAlgorithm)) {
    	                keySizeComboBox.addItem("256"); // ECC commonly uses 256, 384, 521 bit
    	                keySizeComboBox.addItem("384");
    	                keySizeComboBox.addItem("521");
    	            } else if ("DSA".equals(selectedAlgorithm)) {
    	                keySizeComboBox.addItem("1024");
    	                keySizeComboBox.addItem("2048");
    	                keySizeComboBox.addItem("3072");
    	            }
    	            keySizeComboBox.setSelectedIndex(-1); // Reset selection
    	        });

    	        // Continue button
    	        JButton continueButton = new JButton("Continue");
    	        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	        continueButton.addActionListener(e -> {
    	            warningPanel.removeAll(); // Clear previous warnings
    	            boolean hasErrors = false;
    	            boolean hasWarnings = false;
    	            StringBuilder warnings = new StringBuilder();

    	            // Validate algorithm
    	            String algorithm = (String) algorithmComboBox.getSelectedItem();
    	            if (algorithm == null || algorithm.isEmpty()) {
    	                JLabel errorLabel = new JLabel("Algorithm must be selected.");
    	                errorLabel.setForeground(Color.RED);
    	                warningPanel.add(errorLabel);
    	                hasErrors = true;
    	            }

    	            // Validate key size
    	            String keySize = (String) keySizeComboBox.getSelectedItem();
    	            if (keySize == null || keySize.isEmpty()) {
    	                JLabel errorLabel = new JLabel("Key size must be selected.");
    	                errorLabel.setForeground(Color.RED);
    	                warningPanel.add(errorLabel);
    	                hasErrors = true;
    	            }

    	            // Validate export keys
    	            String exportOption = (String) exportKeysComboBox.getSelectedItem();
    	            if (exportOption == null || exportOption.equals(null)) {
    	                JLabel errorLabel = new JLabel("Export key option must be selected.");
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
    	                    System.out.println("Continuing with warnings...");
    	                    selectSecurityModules(frame);
    	                }
    	            } else {
    	            	selectSecurityModules(frame);
    	                System.out.println("Algorithm: " + algorithm);
    	                System.out.println("Key Size: " + keySize);
    	                System.out.println("Comments: " + commentsField.getText());
    	                System.out.println("Export Option: " + exportOption);
    	            }
    	        });
    	        mainPanel.add(continueButton);
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

    	        // Continue button
    	        JButton continueButton = new JButton("Continue");
    	        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	        continueButton.addActionListener(e -> {
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
    	        });
    	        mainPanel.add(continueButton);
    	    });
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
    	        JButton browseButton = new JButton("...");
    	        browseButton.setPreferredSize(new Dimension(40, 30));

    	        filePathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, browseButton.getPreferredSize().height));

    	        fileChooserPanel.add(filePathField);
    	        fileChooserPanel.add(Box.createHorizontalStrut(10)); // Add spacing
    	        fileChooserPanel.add(browseButton);

    	        // File chooser action listener
    	        browseButton.addActionListener(e -> {
    	            JFileChooser fileChooser = new JFileChooser();
    	            int result = fileChooser.showOpenDialog(frame);
    	            if (result == JFileChooser.APPROVE_OPTION) {
    	                filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
    	            }
    	        });

    	        mainPanel.add(fileChooserPanel);

    	        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing

    	        // Create button
    	        JButton createButton = new JButton("Create my own key");
    	        createButton.setPreferredSize(new Dimension(200, 40));
    	        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	        createButton.addActionListener(e -> createGPGIdentity(frame));
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
    	
    	public void createAccount(JFrame frame) {
    	// Ensure this method runs on EDT (Event Dispatch Thread for stability of program)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        frame.setTitle("AmnesicChat - Create Account");

        // Base size for the window to equally show storage devices.
        List<String> availableDevices = getAvailableStorageDevices();
        int aD = availableDevices.size();

        // Calculate additional height based on the number of devices for height optimisation.
        frameUpdate(frame, aD);

        frame.getContentPane().removeAll();

        // Create a panel for creating account
        JPanel createAccountPanel = new JPanel();
        createAccountPanel.setLayout(new BoxLayout(createAccountPanel, BoxLayout.Y_AXIS));
        createAccountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Header label
        JLabel headerLabel = new JLabel("Create Device Lock", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountPanel.add(headerLabel);

        createAccountPanel.add(Box.createVerticalStrut(10)); // Add spacing

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

        strictModePanel.add(yesButton);
        strictModePanel.add(Box.createHorizontalStrut(10)); // Add spacing
        strictModePanel.add(noButton);
        createAccountPanel.add(strictModePanel);

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

        // Device toggle buttons (for devices available)
        JPanel devicePanel = new JPanel();
        devicePanel.setLayout(new GridLayout(0, 1, 10, 10));  // Grid layout for device toggle buttons

        // Create an array to store the toggled states
        List<JToggleButton> toggleButtons = new ArrayList<>();

        for (String device : availableDevices) {
            JToggleButton deviceToggleButton = new JToggleButton(device);
            deviceToggleButton.setToolTipText("Click to select " + device);
            deviceToggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add the toggle button to the list
            toggleButtons.add(deviceToggleButton);
            devicePanel.add(deviceToggleButton);
        }

        createAccountPanel.add(devicePanel);
        createAccountPanel.add(Box.createVerticalStrut(20)); // Add spacing

        // Continue button
        JButton continueButton = new JButton("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertGPGIdentity(frame);
            }
        });
        createAccountPanel.add(continueButton);
        createAccountPanel.add(Box.createVerticalStrut(10)); // Add spacing
        // Back button (to go back to the main menu)
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu(frame);  // Go back to main menu
            }
        });
        createAccountPanel.add(backButton);

        JPanel modeSelectionPanel = new JPanel();
        modeSelectionPanel.setLayout(new BoxLayout(modeSelectionPanel, BoxLayout.X_AXIS));

        JLabel modeLabel = new JLabel("Choose Device Source: ");
        modeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        modeSelectionPanel.add(modeLabel);

        // Radio buttons for mode selection
        JRadioButton hardwareRadioButton = new JRadioButton("Hardware Info", true);
        JRadioButton fileSystemRadioButton = new JRadioButton("File System Roots");

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(hardwareRadioButton);
        modeGroup.add(fileSystemRadioButton);

        
        // Action listeners for each radio button to switch modes
        hardwareRadioButton.addActionListener(e -> {
            useHardwareInfoMode = true;
            updateDeviceList(createAccountPanel);
            frameUpdate(frame, aD);
        });

        fileSystemRadioButton.addActionListener(e -> {
            useHardwareInfoMode = false;
            updateDeviceList(createAccountPanel);
            frameUpdate(frame, aD);
        });

        // Add the radio buttons to the mode selection panel
        modeSelectionPanel.add(hardwareRadioButton);
        modeSelectionPanel.add(fileSystemRadioButton);
        createAccountPanel.add(Box.createVerticalStrut(20)); // Add spacing
        createAccountPanel.add(modeSelectionPanel);
        
        // Add this panel and contents to the frame
        frame.getContentPane().add(createAccountPanel, BorderLayout.CENTER);
       
        // Refresh the frame
        frame.revalidate();
        frame.repaint();
    }
        });
    }
    // Method to update the navigation buttons' enable/disable state
    public void updateNavigationButtons(JButton backButton, JButton nextButton, int currentPage, int totalPages) {
        backButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(currentPage < totalPages - 1);
    }


    public void mainMenu(JFrame frame) {
        // Ensure this method runs on EDT (Event Dispatch Thread for stability of program)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Initialise the frame
                frame.setTitle("AmnesicChat - Account");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(650, 450);
                frame.getContentPane().removeAll();
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
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
                frame.add(mainPanel, BorderLayout.CENTER);

                // Banner setup
                JLabel imageLabel = new JLabel();
                URL imageURL = getClass().getResource("/images/AmnesicLabel.png");
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
                        SwingConstants.CENTER);
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
                            }
                        }
                    });

                    // Add the components of file choosing to the main panel
                    mainPanel.add(fileChooserPanel);
                } else {
                    System.out.println("File button icon not found");
                }

                mainPanel.add(Box.createVerticalStrut(20)); // Add spacing

                // Create Account button
                JButton createAccountButton = new JButton("Create Account");
                createAccountButton.setPreferredSize(new Dimension(200, 40));
                createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                createAccountButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        createAccount(frame);
                    }
                });
                mainPanel.add(createAccountButton);

                // Listen to the text field for changes
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

                    // Method to update the button text based on the file path field
                    private void updateButtonText() {
                        if (filePathField.getText().trim().isEmpty()) {
                            createAccountButton.setText("Create Account");
                        } else {
                            createAccountButton.setText("Load Account");
                        }
                    }
                });

                // Set frame visible
                frame.setVisible(true);
            }
        });
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
