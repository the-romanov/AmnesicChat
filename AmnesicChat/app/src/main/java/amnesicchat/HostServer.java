import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class HostServer {
    private JComboBox<String> serverDropdown;
    private JButton refreshButton, previousButton, nextButton, createServerButton, useServerButton, backButton;
    private JLabel pageLabel;
    private DefaultComboBoxModel<String> serverModel;

    //Access the instances
    static App app = CentralManager.getApp();
    
    static CreateServer createServer = CentralManager.getCreateServer();

    private static int portNumber;
    private static String serverFileName;
    private static String joinPassword;
    private static List<String> whitelist;
    private static List<String> blacklist;
    private static int rateLimit;
    private static String serverKey;

    public static void hostLiveServer(File decryptedFile) {
        // Step 1: Read and decrypt the file (assuming it's already decrypted)
        String fileContents = decryptServerFile(decryptedFile);

        // Step 2: Extract values from the decrypted content
        if (fileContents != null) {
            parseServerConfig(fileContents);
        } else {
            JOptionPane.showMessageDialog(null, "Error decrypting the server file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 3: Create the new JFrame for server hosting status
        JFrame hostingFrame = new JFrame("Server Hosting Status");
        hostingFrame.setLayout(new BoxLayout(hostingFrame.getContentPane(), BoxLayout.Y_AXIS));

        // Server Name and Port Label
        JLabel serverNameLabel = new JLabel("Server Name: " + serverFileName);
        JLabel portLabel = new JLabel("Port: " + portNumber);

        // Settings Button
        JButton settingsButton = new JButton("Server Settings");
        settingsButton.addActionListener(e -> {
            // Optionally open server settings UI here
            JOptionPane.showMessageDialog(hostingFrame, "Settings button clicked.", "Settings", JOptionPane.INFORMATION_MESSAGE);
        });

        // Close Button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            hostingFrame.dispose(); // Close the hosting UI when clicked
            // Optionally shutdown the server or perform cleanup
        });

        hostingFrame.add(serverNameLabel);
        hostingFrame.add(portLabel);
        hostingFrame.add(settingsButton);
        hostingFrame.add(closeButton);

        hostingFrame.setSize(400, 300);
        hostingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hostingFrame.setVisible(true);

        // Now we will open the server socket and listen for connections
        openServerSocket();
    }

    // Helper method to decrypt the server configuration file
    private static String decryptServerFile(File decryptedFile) {
        try {
            // Assuming a simple read of the decrypted content from the file
            // In practice, you may need to apply your decryption logic here
            StringBuilder fileContentBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(decryptedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContentBuilder.append(line);
                }
            }
            return fileContentBuilder.toString();
        } catch (IOException ex) {
            System.err.println("Error reading decrypted file: " + ex.getMessage());
            return null;
        }
    }

    // Helper method to parse the decrypted content and extract the relevant details
    private static void parseServerConfig(String config) {
        // Split the config by ':' to get each value
        String[] parts = config.split(":");
        
        if (parts.length == 6) {
            try {
                // Extract and parse the fields
                joinPassword = parts[0]; // Decrypted join password
                whitelist = Arrays.asList(parts[1].split(","));
                blacklist = Arrays.asList(parts[2].split(","));
                rateLimit = Integer.parseInt(parts[3]);
                portNumber = Integer.parseInt(parts[4]);
                serverKey = parts[5]; // Server key
            } catch (NumberFormatException ex) {
                System.err.println("Error parsing configuration: " + ex.getMessage());
            }
        } else {
            System.err.println("Invalid server configuration format.");
        }
    }

    // Method to open the server socket and handle connections
    public static void openServerSocket() {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server started on port " + portNumber);
            while (true) {
                // Wait for a client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Ping the client
                pingClient(clientSocket);
            }
        } catch (IOException ex) {
            System.err.println("Error starting the server: " + ex.getMessage());
        }
    }

    // Method to send a ping to the client
    public static void pingClient(Socket clientSocket) {
        try {
            OutputStream out = clientSocket.getOutputStream();
            out.write("PING".getBytes()); // Send a simple PING message to the client
            System.out.println("Ping sent to client at " + clientSocket.getInetAddress().getHostAddress());
        } catch (IOException ex) {
            System.err.println("Error pinging the client: " + ex.getMessage());
        }
    }
    
    public void hostServer(JFrame frame) {
    	frame.getContentPane().removeAll();
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Instruction Label
        JLabel instructionLabel = new JLabel("Load a server from the dropdown below. Otherwise place it under the servers folder.");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        frame.add(instructionLabel, gbc);

        // Server Dropdown
        serverModel = new DefaultComboBoxModel<>();
        serverDropdown = new JComboBox<>(serverModel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(serverDropdown, gbc);

        // Select Button
        JButton selectButton = new JButton("Select");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        frame.add(selectButton, gbc);

        // Navigation Buttons
        refreshButton = new JButton("Refresh");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(refreshButton, gbc);

        previousButton = new JButton("Previous");
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(previousButton, gbc);

        nextButton = new JButton("Next");
        gbc.gridx = 2;
        gbc.gridy = 2;
        frame.add(nextButton, gbc);

        // Page Label
        pageLabel = new JLabel("Page 1/3");
        pageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(pageLabel, gbc);

        // Bottom Buttons
        createServerButton = new JButton("Create Server");
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(createServerButton, gbc);

        useServerButton = new JButton("Use Server");
        gbc.gridx = 1;
        gbc.gridy = 4;
        frame.add(useServerButton, gbc);

        backButton = new JButton("Back");
        gbc.gridx = 2;
        gbc.gridy = 4;
        frame.add(backButton, gbc);

        frame.setVisible(true);

        // Load Servers and Add Listeners
        addActionListeners(frame);
        loadServerFiles();
    }

    private void loadServerFiles() {
        serverModel.removeAllElements();
        File serverFolder = new File("./servers");
        if (serverFolder.exists() && serverFolder.isDirectory()) {
            File[] files = serverFolder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    serverModel.addElement(file.getName());
                }
            }
        } else {
            serverModel.addElement("No servers found");
        }
    }

    private void addActionListeners(JFrame frame) {
        refreshButton.addActionListener(e -> loadServerFiles());
        previousButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Previous clicked!"));
        nextButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Next clicked!"));
        createServerButton.addActionListener(e -> createServer.createServer1(frame));
        useServerButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Use Server clicked!"));
        backButton.addActionListener(e -> app.loggedInMenu(frame, null, null));
    }
}
