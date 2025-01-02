import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import java.util.List;
import java.util.ArrayList;

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
                JOptionPane.showMessageDialog(frame, "Device Lock settings saved!");
                // Implement further logic for saving settings here
            }
        });
        createAccountPanel.add(continueButton);

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
                // Initialize the frame
                frame.setTitle("AmnesicChat - Account");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(650, 400);
                frame.getContentPane().removeAll();
                frame.setLayout(new BorderLayout());

                // Set icon for the frame
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
