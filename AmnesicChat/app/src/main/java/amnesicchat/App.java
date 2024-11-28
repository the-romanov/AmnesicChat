import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

public class App {

    public static void main(String[] args) {
        // Create a new JFrame (window)
        JFrame frame = new JFrame("Predefined Image Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set the size of the window

        // Set up the layout manager for the frame
        frame.setLayout(new FlowLayout());

        // Create a label to display the predefined file path
        JLabel fileLabel = new JLabel("Predefined Image Loaded.");
        
        // Create a label to display the image
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(400, 300)); // Set a preferred size for the image label

        // Load the image from the resources folder
        try {
            URL imageURL = App.class.getClassLoader().getResource("resources/images/myimage.jpg");
            if (imageURL != null) {
                ImageIcon imageIcon = new ImageIcon(ImageIO.read(imageURL));
                imageLabel.setIcon(imageIcon);
            } else {
                fileLabel.setText("Image file not found.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add the file label and image label to the frame
        frame.add(fileLabel);
        frame.add(imageLabel);

        // Make the window visible
        frame.setVisible(true);
    }
}
