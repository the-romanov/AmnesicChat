public class CentralManager {
    // Declare instances of the required classes
	private static App app = new App();
	private static ChatSession chatSession = new ChatSession();
	private static CreateAccount createAccount = new CreateAccount();
    private static CreateServer createServer = new CreateServer();
    private static DirectoryServer directoryServer = new DirectoryServer();
    private static final Hash hash = new Hash();
    private static JoinPeerToPeer joinPeerToPeer = new JoinPeerToPeer();
    private static JoinServer joinServer = new JoinServer();
    private static Settings settings = new Settings();
    
    // Getter methods to access instances (stops duplication and promotes optimisation)
    public static App getApp() {return app;}
    public static ChatSession getChatSession() {return chatSession;}
    public static CreateAccount getCreateAccount() {return createAccount;}
    public static CreateServer getCreateServer() {return createServer;}
    public static DirectoryServer getDirectoryServer() {return directoryServer;}
    public static Hash getHash() {
        if (hash != null) {
            return hash;
        } else {
            return new Hash(); // Stops program crashing
        }
    }
    public static JoinPeerToPeer getJoinPeerToPeer() {return joinPeerToPeer;}
    public static JoinServer getJoinServer() {return joinServer;}
    public static Settings getSettings() {return settings;}
}
