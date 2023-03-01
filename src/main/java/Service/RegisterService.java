package Service;
import DAO.*;
import Model.*;
import Request.RegisterRequest;

/**
 * the class for handling the Register request
 */
public class RegisterService {


    /**
     * constructor for register Service. uses the requestLoad class to turn the json file into usable data, then sets the objects to their respective values. Calls the other functions as well.
     * @param request the Register class that holds the translated data from the given json text
     */
    public RegisterService(RegisterRequest request) {}

    /**
     * generates a personID for the new user
     * @return a personID to use
     */
    public String GeneratePersonID() {return null;}

    /**
     * generates an authtoken for the new user, and returns it
     * @return an authtoken string
     */
    public String GenerateAuthtoken() {return null;}

    /**
     * updates the databases with the new user, persons, authtokens, ect.
     */
    public void updateDatabases(){}

    /**
     * calls the fill function to make 4 generations with the new user as the starting person
     */
    public void fill(){}

}
