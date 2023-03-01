package Model;

import java.util.Objects;

/**
 * A user of the server. Contains all the information and identification about a given server user
 */
public class User {

    /**Unique username for user
     */
    private String username;

    /**User's password
     */
    private String password;

    /**User's email address
     */
    private String email;

    /**User's first name
     */
    private String firstName;

    /**User's last name
     */
    private String lastName;

    /**User's gender, can be "f" or "m"
     */
    private String gender;

    /**Unique Person ID assigned to this user's generated Person
     */
    private String personID;


    /**
     * constructor class for User
     * @param username unique username of user
     * @param password password for user
     * @param email email address of user
     * @param firstName first name of user
     * @param lastName last name of user
     * @param gender gender of user, f or m
     * @param personID unique personID of user from their associated Person object
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }



    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(gender, user.gender) && Objects.equals(personID, user.personID);
    }
}
