package Request;

/**
 * class used to handle taking in the register request json text
 */
public class RegisterRequest {

    /**
     * unique username of the user
     */
    private String username;

    /**
     * password for the user
     */
    private String password;

    /**
     * email address of user
     */
    private String email;

    /**
     * first name of user
     */
    private String firstName;

    /**
     * last name of user
     */
    private String lastName;

    /**
     * gender of the user, either f or m
     */
    private String gender;

    /**
     * constructor for this class. Will take in the json string and set all the variables to the specified values.
     * @param jsonString the json string/Request body
     */
    public RegisterRequest(String jsonString) {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
