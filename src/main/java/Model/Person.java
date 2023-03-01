package Model;

import java.util.Objects;

/**
 * class for person object. A person and all attributes held within the database.
 */
public class Person {

    //Unique identifier for this person
    private String personID;

    //Username of user to which this person belongs
    private String associatedUsername;

    //Person's first name
    private String firstName;

    //Person's last name
    private String lastName;

    //Person's gender, can be "f" or "m"
    private String gender;

    //Person ID of person's father, may be null;
    private String fatherID;

    //Person ID of person's mother
    private String motherID;

    //personID of person's spouse, may be null;
    private String spouseID;

    /**
     * constructor of Person object
     * @param personID ID of the person
     * @param associatedUsername associated username for User
     * @param firstName first name of person
     * @param lastName last name of person
     * @param gender gender of person
     * @param fatherID personID of father, may be null
     * @param motherID personID of mother, may be null
     * @param spouseID personID of spouse, may be null
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }



    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) && Objects.equals(associatedUsername, person.associatedUsername) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(gender, person.gender) && Objects.equals(fatherID, person.fatherID) && Objects.equals(motherID, person.motherID)&& Objects.equals(spouseID, person.spouseID);
    }
}
