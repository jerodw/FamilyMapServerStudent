package Request;
import Model.*;
import java.util.*;

/**
 * class used to handle taking in the load request json text
 */
public class LoadRequest {

    /**
     * container for all the users to add
     */
    private Vector<User> users = new Vector<>();

    /**
     * container for all the Person objects to add
     */
    private Vector<Person> persons = new Vector<>();

    /**
     * container for all the event objects to add
     */
    private Vector<Event> events = new Vector<>();

    /**
     * constructor for this class. Will take in the json string and set all the variables to the specified values.
     * @param jsonString the json string/Request body
     */
    public LoadRequest(String jsonString) {}

    public Vector<User> getUsers() {
        return users;
    }

    public void setUsers(Vector<User> users) {
        this.users = users;
    }

    public Vector<Person> getPersons() {
        return persons;
    }

    public void setPersons(Vector<Person> persons) {
        this.persons = persons;
    }

    public Vector<Event> getEvents() {
        return events;
    }

    public void setEvents(Vector<Event> events) {
        this.events = events;
    }
}
