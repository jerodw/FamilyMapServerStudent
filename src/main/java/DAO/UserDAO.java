package DAO;
import Model.Event;
import Model.User;
import java.util.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.sql.*;

/**
 * class used to access the User objects
 */
public class UserDAO {

    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * create a new User object to add to the database of users
     * @param user unique user object
     */
    public void insert(User user) throws DataAccessException{
        String sql = "INSERT INTO Users (username, password, email, firstName, lastName, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (find(user.getUsername()) != null) {
                throw new DataAccessException("Error encountered while inserting an event into the database");
            }

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }

    /**
     * returns a User object given a requested username
     * @param username unique username of user
     * @return the User object that is associated with said user
     */
    public User find(String username) throws DataAccessException{
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM Users WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("PersonID"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * delete a user from the database
     * @param username the unique username of the user to be deleted.
     */
    public void deleteUser(String username) {}

    /**
     * wipes the user database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }
}
