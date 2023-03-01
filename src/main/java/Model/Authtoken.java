package Model;

/**
 * data held for the Authtoken object, which is used to identify Usernames
 */
public class Authtoken {

    /**
     * Unique authtoken
     */
    private String authoken;

    /**
     * Username that is associated with the authtoken
     */
    private String username;

    /**
     * constructor for authoken object
     * @param authtoken unique authoken
     * @param username username associated with authtoken
     */
    public Authtoken(String authtoken, String username) {
        this.authoken = authtoken;
        this.username = username;
    }

    public String getAuthoken() {
        return authoken;
    }

    public void setAuthoken(String authoken) {
        this.authoken = authoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
