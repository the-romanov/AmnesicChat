import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.filechooser.FileNameExtensionFilter;



public class App {
	
	private JFrame frame; //JFrame is private so that we can isolate the variable to prevent potential tampering.
	
	public App() {
		frame = new JFrame("AmnesicChat");
	}
	
	public JFrame getJFrame() {
		return frame;
	}
	
	public void createAccount(JFrame frame) {
	    frame.setTitle("AmnesicChat - Create Account");
	    frame.setSize(650, 500);

	    // Clear existing content
	    frame.getContentPane().removeAll();

	    // Create a panel for creating account (merged code logic)
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

	    // Device dropdowns
	    JPanel devicePanel = new JPanel();
	    devicePanel.setLayout(new GridLayout(4, 1, 10, 10));

	    for (int i = 1; i <= 4; i++) {
	        JComboBox<String> deviceDropdown = new JComboBox<>(
	                new String[]{"Device" + i + " - DeviceName" + i, "Device" + i + " - Option2", "Device" + i + " - Option3"});
	        devicePanel.add(deviceDropdown);
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
	        }
	    });
	    createAccountPanel.add(continueButton);

	    // Add this panel to the frame
	    frame.getContentPane().add(createAccountPanel, BorderLayout.CENTER);

	    // Refresh the frame
	    frame.revalidate();
	    frame.repaint();
	}

	
	public void mainMenu(JFrame frame) {
		// Initialise the frame
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
                createAccount(frame);
            }
        });
        mainPanel.add(createAccountButton);

        // Set frame visible. We must set this otherwise our UI will be hidden.
        frame.setVisible(true);
    }
	

	public static void main(String[] args) {
		App app = new App();
		
		JFrame frame = app.getJFrame();
		
		app.mainMenu(frame);
}
	}