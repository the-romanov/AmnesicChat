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

    private String selectedFolder = null;
    private static int portNumber;
    private static String serverFileName;
    private static String joinPassword;
    private static List<String> whitelist;
    private static List<String> blacklist;
    private static int rateLimit;
    private static String serverKey;
    
    private DefaultListModel<String> serverListModel;
    private JList<String> serverList;
    private int currentPage = 1;
    private final int serversPerPage = 2;
    private File[] serverFiles;

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
        hostingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hostingFrame.setSize(400, 400);
        hostingFrame.setLocationRelativeTo(null); // Center the window

        // Create main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        // Title Label
        JLabel titleLabel = new JLabel("Server Now Hosting On Port: " + portNumber, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        // Server File Name Label
        JLabel serverFileLabel = new JLabel("Server File: " + serverFileName, SwingConstants.CENTER);
        serverFileLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        serverFileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(serverFileLabel);

        // Server Types Label (Placeholder)
        JLabel serverTypesLabel = new JLabel("Server Types: work", SwingConstants.CENTER);
        serverTypesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        serverTypesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(serverTypesLabel);

        // Spacing
        panel.add(Box.createVerticalStrut(10));

        // Close Server Button
        JButton closeButton = new JButton("Close Server & Lock File.");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> {
            hostingFrame.dispose(); // Close the hosting UI
            // Add server shutdown logic if needed
        });
        panel.add(closeButton);

        // Main Menu Button
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuButton.addActionListener(e -> {
            hostingFrame.dispose(); // Close current frame
        });
        panel.add(mainMenuButton);

        // Server Settings Button
        JButton settingsButton = new JButton("Server Settings");
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(hostingFrame, "Settings button clicked.", "Settings", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(settingsButton);

        // Spacing
        panel.add(Box.createVerticalStrut(10));

        // Info Label
        JLabel infoLabel = new JLabel("Connect to the server(s) to be able to do admin actions.", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(infoLabel);

        // Add panel to frame
        hostingFrame.add(panel);
        
        // Force UI refresh
        hostingFrame.revalidate();
        hostingFrame.repaint();
        
        hostingFrame.setVisible(true);

        // Open server socket for live hosting
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
    
    private File lastSelectedDirectory = null; // Store last directory

    private void listTextFilesInDirectory(File directory) {
        lastSelectedDirectory = directory; // Save directory for refresh
        File[] textFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        serverModel.removeAllElements(); // Clear dropdown

        if (textFiles != null && textFiles.length > 0) {
            for (File file : textFiles) {
                serverModel.addElement(file.getAbsolutePath()); // Add files to dropdown
            }
            JOptionPane.showMessageDialog(null, "Loaded " + textFiles.length + " text files.", "Files Loaded", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No .txt files found in the selected directory.", "No Files Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void selectDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select a Directory");

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(null, "Selected Directory: " + selectedDirectory.getAbsolutePath(), "Directory Selected", JOptionPane.INFORMATION_MESSAGE);
            
            // List only .txt files in the directory
            listTextFilesInDirectory(selectedDirectory);
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
        JLabel instructionLabel = new JLabel("Load a server from the dropdown below. Otherwise, place it under the servers folder.");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(instructionLabel, gbc);

        // Server Dropdown
        serverModel = new DefaultComboBoxModel<>();
        serverDropdown = new JComboBox<>(serverModel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(serverDropdown, gbc);

        // Browse Button (Now Selects a Directory & Filters .txt Files)
        JButton browseButton = new JButton("Browse");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(browseButton, gbc);

        // Refresh Button (Now Refreshes the Loaded Files)
        refreshButton = new JButton("Refresh");
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(refreshButton, gbc);

        // Bottom Buttons
        createServerButton = new JButton("Create Server");
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(createServerButton, gbc);

        useServerButton = new JButton("Use Server");
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(useServerButton, gbc);

        backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        frame.add(backButton, gbc);

        frame.setVisible(true);

        // Add Functionality
        browseButton.addActionListener(e -> selectServerFolder());
        refreshButton.addActionListener(e -> refreshServerFiles());
        useServerButton.addActionListener(e -> useSelectedServer(frame));
        
        addActionListeners(frame);
        loadServerFiles();
    }

    // Reloads .txt files from last selected directory
    private void refreshServerFiles() {
        if (lastSelectedDirectory != null) {
            listTextFilesInDirectory(lastSelectedDirectory);
        } else {
            JOptionPane.showMessageDialog(null, "No directory selected. Please select a folder first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Loads server files (directories)
    private void loadServerFiles() {
        serverModel.removeAllElements();
        File serverFolder = new File(selectedFolder != null ? selectedFolder : "./servers");
        if (serverFolder.exists() && serverFolder.isDirectory()) {
            File[] files = serverFolder.listFiles(File::isDirectory);
            if (files != null) {
                for (File file : files) {
                    serverModel.addElement(file.getName());
                }
            }
        } else {
            serverModel.addElement("No servers found");
        }
    }

    // Browse for a folder and list .txt files
    private void selectServerFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select a Directory");

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            listTextFilesInDirectory(selectedDirectory);
        }
    }

    // Add listeners for buttons
    private void addActionListeners(JFrame frame) {
        createServerButton.addActionListener(e -> createServer.createServer1(frame));
        useServerButton.addActionListener(e -> useSelectedServer(frame));
        backButton.addActionListener(e -> app.loggedInMenu(frame, null, null));
    }

    // Use the selected server
    private void useSelectedServer(JFrame frame) {
        String selectedServer = (String) serverDropdown.getSelectedItem();
        if (selectedServer != null && !selectedServer.equals("No servers found")) {
            JOptionPane.showMessageDialog(frame, "Using server: " + selectedServer);
            // Add logic here to start the server or perform actions
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a valid server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
