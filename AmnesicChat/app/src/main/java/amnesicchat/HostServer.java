import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class HostServer {
    private JComboBox<String> serverDropdown;
    private JButton refreshButton, previousButton, nextButton, createServerButton, useServerButton, backButton;
    private JLabel pageLabel;
    private DefaultComboBoxModel<String> serverModel;

    //Access the instances
    static App app = CentralManager.getApp();
    
    static CreateServer createServer = CentralManager.getCreateServer();
 
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
