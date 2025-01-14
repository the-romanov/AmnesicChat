import javax.swing.*;
import java.awt.*;

public class DirectoryServer {
	public void directoryServer(JFrame frame) {
	    // Clear frame
	    frame.getContentPane().removeAll();
	    
	    // Set up frame properties
	    frame.setTitle("Directory Server");
	    frame.setLayout(new BorderLayout());
	    frame.setSize(900, 600);

	    // Left Panel for buttons and user details
	    JPanel leftPanel = new JPanel();
	    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
	    leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // Add user information
	    JLabel usernameLabel = new JLabel("USERNAME: progamer441");
	    usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    leftPanel.add(usernameLabel);
	    leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

	    // Add buttons with margin
	    String[] buttons = {
	        "REFRESH LIST", 
	        "SHOW CONTACTS", 
	        "SHOW DEVICE ID CHANGED", 
	        "SHOW GPG KEY CHANGED", 
	        "SHOW USER NAME CHANGE", 
	        "CONNECT TO ANOTHER SERVER", 
	        "DISCONNECT FROM THIS SERVER"
	    };
	    for (String btnText : buttons) {
	        JButton button = new JButton(btnText);
	        button.setAlignmentX(Component.CENTER_ALIGNMENT);
	        button.setPreferredSize(new Dimension(200, 40)); // Adjust button size
	        leftPanel.add(button);
	        leftPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Margin between buttons
	    }

	    // Add left panel to frame
	    frame.add(leftPanel, BorderLayout.WEST);

	    // Center Panel for user list
	    JPanel centerPanel = new JPanel();
	    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
	    JScrollPane scrollPane = new JScrollPane(centerPanel);

	    // Add user entries
	    String[][] userEntries = {
	        {"User: vegas1255", "Fingerprint: 2q-8A_*4jojsa89u%%£;", "Relation: None", "WHITE"},
	        {"User: vegas1255", "Fingerprint: aw54Al_)asdasf-=et6w", "Relation: Contact List", "PINK"},
	        {"User: vegas1254", "Fingerprint: awrA%£P[ksdfpk-03q5", "Relation: Device Mismatch", "YELLOW"},
	        {"User: mega223", "Fingerprint: 90sauMO4maosimfwa4", "Relation: GPG Mismatch", "RED"},
	        {"User: vegas1255", "Fingerprint: Akafs=K\"$-0sdpmfrasr", "Relation: Identity Flush", "RED"},
	        {"User: abaaaaasus", "Fingerprint: 25)(\"U^A)Qj092q4j\\980", "Relation: Partial Authorisation (You)", "PURPLE"},
	        {"User: abacus", "Fingerprint: 5,loa,w)(\"$q24waraw0", "Relation: Authorised Friend", "BLUE"},
	        {"User: absus", "Fingerprint: 4jo*(]qnm5lkstdg8p8w", "Relation: Partial Authorisation (User)", "PURPLE"}
	    };

	    for (String[] entry : userEntries) {
	        JPanel userPanel = new JPanel();
	        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
	        userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        userPanel.setBackground(Color.decode(entry[3].equals("WHITE") ? "#FFFFFF" :
	                              entry[3].equals("PINK") ? "#FFC0CB" :
	                              entry[3].equals("YELLOW") ? "#FFFF00" :
	                              entry[3].equals("RED") ? "#FF0000" :
	                              entry[3].equals("PURPLE") ? "#800080" : "#0000FF"));
	        for (int i = 0; i < 3; i++) {
	            JLabel label = new JLabel(entry[i]);
	            label.setPreferredSize(new Dimension(600, 20)); // Expand labels to the right
	            userPanel.add(label);
	        }
	        centerPanel.add(userPanel);
	        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Margin between user panels
	    }

	    // Add scroll pane to center panel
	    frame.add(scrollPane, BorderLayout.CENTER);

	    // Bottom Panel for navigation (Pagination)
	    JPanel bottomPanel = new JPanel();
	    bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

	    JButton previousButton = new JButton("PREVIOUS");
	    JButton nextButton = new JButton("NEXT");

	    bottomPanel.add(new JLabel("PAGE 1/3"));
	    bottomPanel.add(previousButton);
	    bottomPanel.add(nextButton);

	    // Add bottom panel to frame
	    frame.add(bottomPanel, BorderLayout.SOUTH);

	    // Revalidate and repaint the frame to ensure layout is updated
	    frame.revalidate();
	    frame.repaint();
	}
}