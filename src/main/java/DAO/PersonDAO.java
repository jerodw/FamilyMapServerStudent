package DAO;
import Model.Person;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Data access object class for Person Class
 */
public class PersonDAO {

    private final Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * adds a person object to the person database
     * @param person the person object being added to the database
     */
    public void insert(Person person) throws DataAccessException{
        String sql = "INSERT INTO Events (personID, AssociatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }

    /**
     * finds a specific Person object based on a given personID
     * @param personID the unique ID of the person
     * @return The person object related to the personID
     */
    public Person find(String personID) throws DataAccessException{
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * removes a person from the database based on personID
     * @param personID
     */
    public void deletePerson(String personID) {}

    /**
     * returns all persons who are linked by the given associatedUsername
     * @param associatedUsername username of the user who owns all the persons
     * @return vector of all persons who are owned by the user
     */
    public Vector<Person> getPerson(String associatedUsername) {return null;}

    /**
     * wipes the person database clear
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }
}
