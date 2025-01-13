public class JoinPeerToPeer {
	public void peerToPeerUI(JFrame frame) {
    	// Clear frame
        frame.getContentPane().removeAll();
        
        // Set up the frame properties
        frame.setTitle("Peer To Peer");
        frame.setLayout(new BorderLayout());
        frame.setSize(600, 400);

        // Panel for the title
        JLabel titleLabel = new JLabel("PEER TO PEER", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Main panel for the user list
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Sample user data 
        String[][] users = {
            {"vegas1255", "2q-8A_*4jojsa89u%*£;", "Authorised Friend", "Look at the stars and you will be blinded by light.", "white"},
            {"mega223", "90sauMO4maosimfwa4", "GPG Mismatch", "Just chilling.", "yellow"},
            {"vegas1255", "Akafs=K\"$-0sdpmfrasr", "Identity Flush", "123e", "red"},
            {"abaaaaasus", "25)(*U\"A)Qj092q4j)980", "Partial Authorisation (You)", "Abacus is nice, you.", "purple"}
        };

        // Add users to the panel
        for (int i = 0; i < users.length; i++) {
            String username = users[i][0];
            String fingerprint = users[i][1];
            String relation = users[i][2];
            String note = users[i][3];
            String color = users[i][4];

            JPanel userPanel = new JPanel(new BorderLayout());
            userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            // Set background color
            switch (color.toLowerCase()) {
                case "yellow": userPanel.setBackground(Color.YELLOW); break;
                case "red": userPanel.setBackground(Color.RED); break;
                case "purple": userPanel.setBackground(Color.MAGENTA); break;
                default: userPanel.setBackground(Color.WHITE); break;
            }

            // User info label
            JLabel userInfo = new JLabel(
                "<html><b>" + username + "</b><br>Fingerprint: " + fingerprint +
                "<br>Relation: " + relation + "<br>" + note + "</html>");
            userPanel.add(userInfo, BorderLayout.CENTER);

            // Button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

            JButton startSessionButton = new JButton("Start P2P Session");
            JButton authorizeButton = new JButton(relation.equals("Authorised Friend") ? "Reauthorise User" : "Authorise User");
            buttonPanel.add(startSessionButton);
            buttonPanel.add(authorizeButton);

            userPanel.add(buttonPanel, BorderLayout.EAST);

            // Add the user panel to the main panel
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 1.0;
            gbc.gridwidth = 1;
            mainPanel.add(userPanel, gbc);
        }

        // Add the main panel to the frame
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Footer panel for navigation buttons
        JPanel footerPanel = new JPanel(new FlowLayout());
        JButton previousButton = new JButton("PREVIOUS");
        JButton nextButton = new JButton("NEXT");
        footerPanel.add(previousButton);
        footerPanel.add(nextButton);
        frame.add(footerPanel, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }
}