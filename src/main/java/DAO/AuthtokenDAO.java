package DAO;
import java.util.*;
import Model.Authtoken;

/**
 * class that is used to store and access all the generated authtokens
 */
public class AuthtokenDAO {


    /**
     * adds a new Authtoken object to the Authtoken database, throws an exception if it already exists.
     * @param authtoken unique authoken
     * @param username username associated with authtoken
     */
    public void insertAuthtoken(String authtoken, String username) {}

    /**
     * finds and returns an authtoken object from the authtoken database given the username
     * @param username username associated with authtoken
     * @return the authtoken object associated with the username
     */
    public Authtoken findAuthtoken(String username) {return null;}

    /**
     * removes an Authtoken object from the database, throws an exception if username is not found
     * @param username the associated username of the authtoken to be removed
     */
    public void removeAuthtoken(String username) {}

    /**
     * wipes the Authtoken database
     */
    public void clear() {}
}
