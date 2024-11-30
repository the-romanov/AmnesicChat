import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

public class App {

    public static void main(String[] args) {
        // Create a new JFrame (window)
    	JFrame frame = new JFrame("Image");
    	frame.setTitle("AmnesicChat"); // Set the window title
    	ImageIcon icon = new ImageIcon(("images/Favicon.png")); // Use a relative path to the image file
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set the size of the window

        // Set up the layout manager for the frame
        frame.setLayout(new FlowLayout());
        
        // Create a label to display the image
        JLabel imageLabel = new JLabel();
        ImageIcon originalIcon = new ImageIcon("images/AmnesicLabel.png"); // Load the image
        Image scaledImage = originalIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH); // Scale the image
        imageLabel.setIcon(new ImageIcon(scaledImage)); // Set the scaled image to the label
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image

        // Load the image from the resources folder
        try {
            URL imageURL = App.class.getClassLoader().getResource("images/AmnesicLabel.png");
            if (imageURL != null) {
                ImageIcon imageIcon = new ImageIcon(ImageIO.read(imageURL));
                imageLabel.setIcon(imageIcon);
            } 
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add the file label and image label to the frame
        frame.add(imageLabel);

        // Make the window visible
        frame.setVisible(true);
    }
}
