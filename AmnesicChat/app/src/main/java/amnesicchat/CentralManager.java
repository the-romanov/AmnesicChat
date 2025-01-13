public class CentralManager {
    // Declare instances of the required classes
    private static CreateServer createServer;
    private static Hash hash;
    private static CreateAccount createAccount;
    private static App app;

    // Static block to initialize the instances
    static {
        createServer = new CreateServer();
        hash = new Hash();
        createAccount = new CreateAccount();
        app = new App();
    }

    // Getter methods to access instances
    public static CreateServer getCreateServer() {
        return createServer;
    }

    public static Hash getHash() {
        return hash;
    }

    public static CreateAccount getCreateAccount() {
        return createAccount;
    }
    
    public static App getApp() {
    	return app;
    }
}
