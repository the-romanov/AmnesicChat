import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	public String hashSHA512(String input) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-512");
	        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
	        // Convert byte array to hex string
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : encodedHash) {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) {
	                hexString.append('0');
	            }
	            hexString.append(hex);
	        }
	        return hexString.toString();
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("SHA-512 algorithm not found", e);
	    }
	}
	public String hashSHA256(String input) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");  // Use SHA-256 instead of SHA-512
	        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));  // Hash the input
	        // Convert byte array to hex string
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : encodedHash) {
	            String hex = Integer.toHexString(0xff & b);  // Convert byte to hex
	            if (hex.length() == 1) {
	                hexString.append('0');
	            }
	            hexString.append(hex);  // Append hex string
	        }
	        return hexString.toString();  // Return the hexadecimal string
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("SHA-256 algorithm not found", e);  // Handle exception
	    }
	}

}