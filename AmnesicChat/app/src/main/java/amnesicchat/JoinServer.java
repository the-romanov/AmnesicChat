import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 

public class JoinServer {
	public void connectionUI(JFrame frame) {
		
	    App app = CentralManager.getApp();
		// Clear frame
	    frame.getContentPane().removeAll();
	    
	    // Set up the frame properties
	    frame.setTitle("Connection Settings");
	    frame.setLayout(new GridBagLayout());
	    frame.setSize(500, 300);

	    // Use GridBagConstraints for layout
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 10, 10);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    // Add "IP Address/Domain" label and text field
	    JLabel ipLabel = new JLabel("IP Address/Domain:");
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    frame.add(ipLabel, gbc);

	    JTextField ipField = new JTextField();
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    frame.add(ipField, gbc);

	    // Add "Peer To Peer Port" label and text field
	    JLabel portLabel = new JLabel("Peer To Peer Port:");
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gbc.gridwidth = 1;
	    frame.add(portLabel, gbc);

	    JTextField portField = new JTextField();
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.gridwidth = 2;
	    frame.add(portField, gbc);

	    // Add "Disconnect Servers" label and checkboxes
	    JLabel disconnectLabel = new JLabel("Disconnect Servers on Successful Connection:");
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.gridwidth = 3;
	    frame.add(disconnectLabel, gbc);

	    JCheckBox directoryBox = new JCheckBox("DIRECTORY");
	    JCheckBox chatBox = new JCheckBox("CHAT");
	    JCheckBox pingBox = new JCheckBox("PING");
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.gridwidth = 1;
	    frame.add(directoryBox, gbc);
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    frame.add(chatBox, gbc);
	    gbc.gridx = 2;
	    gbc.gridy = 3;
	    frame.add(pingBox, gbc);

	    // Add "Attempt Connection" button
	    JButton attemptConnectionButton = new JButton("Attempt Connection");
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    gbc.gridwidth = 3;
	    gbc.anchor = GridBagConstraints.CENTER;
	    frame.add(attemptConnectionButton, gbc);

	    // Add Back Button to go back to loggedInMenu
	    JButton backButton = new JButton("Back");
	    gbc.gridx = 0;
	    gbc.gridy = 5;
	    gbc.gridwidth = 3;
	    gbc.anchor = GridBagConstraints.CENTER;
	    frame.add(backButton, gbc);

	    // Action Listener for the Back Button
	    backButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            app.loggedInMenu(frame, null, null); 
	        }
	    });
	}
	
    public void createJoinServerUI(JFrame frame) {
        // Clear frame
        frame.getContentPane().removeAll();
        frame.setSize(600, 400);
        frame.setLayout(null); // Use null layout for custom positioning

        // Title Label
        JLabel titleLabel = new JLabel("Join A Server:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(220, 20, 200, 30);
        frame.add(titleLabel);

        // IP/Domain Label and TextField
        JLabel ipLabel = new JLabel("IP/Domain:");
        ipLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ipLabel.setBounds(50, 80, 100, 20);
        frame.add(ipLabel);

        JTextField ipField = new JTextField();
        ipField.setBounds(150, 80, 350, 25);
        frame.add(ipField);

        // Server Types Label and Buttons
        JLabel serverTypeLabel = new JLabel("Server Types:");
        serverTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        serverTypeLabel.setBounds(50, 130, 100, 20);
        frame.add(serverTypeLabel);

        JToggleButton directoryButton = new JToggleButton("DIRECTORY");
        directoryButton.setBounds(150, 130, 110, 30);
        frame.add(directoryButton);

        JToggleButton chatButton = new JToggleButton("CHAT");
        chatButton.setBounds(270, 130, 110, 30);
        frame.add(chatButton);

        JToggleButton pingButton = new JToggleButton("PING");
        pingButton.setBounds(390, 130, 110, 30);
        frame.add(pingButton);

        // Server Password Label and TextField
        JLabel passwordLabel = new JLabel("Server Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(50, 180, 150, 20);
        frame.add(passwordLabel);

        JTextField passwordField = new JTextField();
        passwordField.setBounds(200, 180, 300, 25);
        frame.add(passwordField);

        // Info Icon and Tooltip
        JLabel infoLabel = new JLabel(new ImageIcon("info_icon.png")); // Replace with the path to your info icon
        infoLabel.setBounds(50, 230, 20, 20);
        frame.add(infoLabel);

        JLabel tooltipLabel = new JLabel("<html>Allows you to connect to different servers that may just have<br>" +
                "only one type of server running only. Example: One address<br>" +
                "can be a ping server, the other can be a chat server.</html>");
        tooltipLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        tooltipLabel.setBounds(80, 230, 400, 50);
        frame.add(tooltipLabel);

        // Buttons for Open Connection and Connect
        JButton openConnectionButton = new JButton("OPEN ANOTHER CONNECTION");
        openConnectionButton.setBounds(50, 300, 220, 30);
        frame.add(openConnectionButton);

        JButton connectButton = new JButton("CONNECT");
        connectButton.setBounds(300, 300, 150, 30);
        frame.add(connectButton);

        frame.revalidate();
        frame.repaint();
    }
}