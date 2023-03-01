package DAO;
import Model.Event;
import com.sun.source.tree.Tree;

import java.util.*;

/**
 * class to access the event database
 */
public class EventDAOLegacy {

    /**
     * adds a new event to the Event Database
     * @param eventID Unique identifier for this event
     * @param associatedUsername Username of owner
     * @param personID ID of owner
     * @param latitude Longitude of event's location
     * @param longitude Longitude of location of event
     * @param country Latitude of location of event
     * @param city city of event
     * @param eventType type of event
     * @param year year of event
     */
    public void insertEvent(String eventID, String associatedUsername, String personID, Float latitude, Float longitude, String country, String city, String eventType, Integer year) {}

    /**
     * finds an event within the events database based on the eventID, returns null if ID cannot be found.
     * @param eventID Unique identifier for the event
     * @return the Event object associated with the eventID
     */
    public Event findEvent(String eventID) {return null;}

    /**
     * removes a specific event from the database based on eventID
     * @param eventID unique identifier for event object
     */
    public void removeEvent(String eventID) {}

    /**
     * wipes the entire map clear
     */
    public void clear(){}

    /**
     * returns all events pertaining to a specific personID. Used for the /event endpoint.
     * @param personID the personID of the user
     * @return a vector of all Events that belong to the user and their ancestors
     */
    public Vector<Event> getEvents(String personID) {return null;}
}
