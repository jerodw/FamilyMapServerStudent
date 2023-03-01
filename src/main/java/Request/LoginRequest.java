package Request;

/**
 * class used to handle taking in the login request json text
 */
public class LoginRequest {

    /**
     * unique username of the user
     */
    private String username;

    /**
     * password for the user
     */
    private String password;

    /**
     * constructor for this class. Will take in the json string and set all the variables to the specified values.
     * @param jsonString the json string/Request body
     */
    public LoginRequest(String jsonString) {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
