import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.lang.NoSuchMethodException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;

import org.pgpainless.PGPainless;
import org.pgpainless.key.generation.KeyRingBuilder;
import org.pgpainless.algorithm.*;
import org.pgpainless.algorithm.KeyFlag;
import org.pgpainless.key.generation.type.*;
import org.pgpainless.key.generation.type.rsa.RSA;
import org.pgpainless.key.generation.type.rsa.*;
import org.pgpainless.key.util.*;
import org.pgpainless.util.Passphrase;
import org.bouncycastle.openpgp.PGPException;

public class App {
	
	/*
	 * IF YOU ARE SEEING THIS SOURCE CODE:
	 * I AM CURRENTLY BRANCHING CODE TO OTHER CLASSES.
	 * IF YOU DO NOT SEE THIS MESSAGE, IT MEANS THE
	 * IMPORTS HAVE BEEN COMPLETED.
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
    CreateServer createServer = CentralManager.getCreateServer();
    
    // Access the Hash instance
    Hash hash = CentralManager.getHash();
    
    // Access the CreateAccount instance
    CreateAccount createAccount = CentralManager.getCreateAccount();
    
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

    	    // Add buttons
    	    String[] buttonLabels = {"JOIN A SERVER", "PEER TO PEER", "HOST A SERVER", "CHANGE ACCOUNT", "QUIT"};
    	    int buttonWidth = 200;
    	    int buttonHeight = 30;
    	    int buttonSpacing = 40;
    	    int initialYPosition = 280; // Starting y-position for the buttons

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
    	                    createJoinServerUI(frame);
    	                }
    	            });
    	        }

    	        // Action listener for "PEER TO PEER" button
    	        if (text.equals("PEER TO PEER")) {
    	            button.addActionListener(new ActionListener() {
    	                public void actionPerformed(ActionEvent e) {
    	                    peerToPeerUI(frame); // Call peerToPeerUI
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
    	
    	public void firstTimeSetup(JFrame frame) {
			// Clear frame
			frame.getContentPane().removeAll();

            // Create the main panel
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Add title label
            JLabel titleLabel = new JLabel("First Time Setup");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(titleLabel);

            // Add a gap
            panel.add(Box.createRigidArea(new Dimension(0, 20)));

            // Add username label and text field
            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(usernameLabel);

            JTextField usernameField = new JTextField(20);
            usernameField.setMaximumSize(new Dimension(300, 25));
            panel.add(usernameField);

            // Add a gap
            panel.add(Box.createRigidArea(new Dimension(0, 20)));

            // Add description label and text area
            JLabel descriptionLabel = new JLabel("Description:");
            descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(descriptionLabel);

            JTextArea descriptionArea = new JTextArea(5, 20);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panel.add(descriptionArea);

            // Add note label
            JLabel noteLabel = new JLabel("(The username & description can be changed at any time later)");
            noteLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noteLabel);

            // Add a gap
            panel.add(Box.createRigidArea(new Dimension(0, 20)));

            // Add continue button
            JButton continueButton = new JButton("Continue");
            continueButton.setFont(new Font("Arial", Font.PLAIN, 14));
            continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            continueButton.addActionListener(e -> {
	        	loggedInMenu(frame, null, null);
	        });
            panel.add(continueButton);

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
                    // Initialise the frame
        	        frame.getContentPane().removeAll();
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
                    appPanel.setLayout(new BoxLayout(appPanel, BoxLayout.Y_AXIS));
                    appPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
                    frame.add(appPanel, BorderLayout.CENTER);

                    // Banner setup
                    JLabel imageLabel = new JLabel();
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

                    // Create the browse button with resized image icon
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
                        appPanel.add(fileChooserPanel);
                    } else {
                        System.out.println("File button icon not found");
                    }

                    appPanel.add(Box.createVerticalStrut(20)); // Add spacing

                    // Create Account button
                    JButton createAccountButton = new JButton("Create Account");
                    createAccountButton.setPreferredSize(new Dimension(200, 40));
                    createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    createAccountButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            createAccount.createAccount(frame);
                        }
                    });
                    appPanel.add(createAccountButton);
                    frame.revalidate();
        	        frame.repaint();
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
