package Model;

import java.util.Objects;

/**
 * class for the event data structure and the data it holds
 */
public class Event {

    /**
     * Unique identifier for this event
     */
    private String eventID;

    /**
     * Username of user to which this event belongs
     */
    private String associatedUsername;

    /**
     * ID of person to which this event belongs
     */
    private String personID;

    /**
     * Latitude of event's location
     */
    private Float latitude;

    /**
     * Longitude of event's location
     */
    private Float longitude;

    /**
     * Country in which event occurred
     */
    private String country;

    /**
     * City in which event occurred
     */
    private String city;

    /**
     * Type of event
     */
    private String eventType;

    /**
     * Year in which event occured
     */
    private Integer year;

    /**
     * constructor for the Event object
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
    public Event(String eventID, String associatedUsername, String personID, Float latitude, Float longitude, String country, String city, String eventType, Integer year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) && Objects.equals(associatedUsername, event.associatedUsername) && Objects.equals(personID, event.personID) && Objects.equals(latitude, event.latitude) && Objects.equals(longitude, event.longitude) && Objects.equals(country, event.country) && Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType) && Objects.equals(year, event.year);
    }
}


