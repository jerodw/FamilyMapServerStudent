package passoff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import logs.InitLogs;
import org.junit.jupiter.api.*;
import passoffmodels.*;
import passoffrequest.*;
import passoffresult.*;
import client.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a mock client used for pass off of the CS240 Family Map Server project.
 * Since the next project is an Android Client, we decided to obfuscate this code
 * in order to preserve the integrity of the learning process for the next project.
 * <p>
 * However, obfuscated code is intentionally hard to read. Therefore, by mercy of
 * the professors, we have included comments for you in order to know exactly what
 * is happening in each test without giving you the actual code.
 * <p>
 * REMINDER: This is the pass off driver. This is not a class of free tests that you
 * can use to debug your own program. Write your own tests first! Verify functionality
 * by running api commands from your own computer using localhost. After your responses
 * look correct on the web api, then you may move on to using this test driver.
 */
public class ServerTest {

    private static final Logger logger;

    static {
        InitLogs.init();
        logger = Logger.getLogger(Client.class.getName());
    }

    private static final User SHEILA = new User("sheila", "parker", "sheila@parker.com", "Sheila", "Parker", "f", "Sheila_Parker");
    private static final User PATRICK = new User("patrick", "spencer", "sheila@spencer.com", "Patrick", "Spencer", "m", "Patrick_Spencer");
    private static final LoginRequest loginRequest = new LoginRequest(SHEILA.getUsername(), SHEILA.getPassword());
    private static final LoginRequest loginRequest2 = new LoginRequest(PATRICK.getUsername(), PATRICK.getPassword());
    private static final RegisterRequest registerRequest = new RegisterRequest(SHEILA.getUsername(), SHEILA.getPassword(), SHEILA.getEmail(), SHEILA.getFirstName(), SHEILA.getLastName(), SHEILA.getGender());
    private static final String BIRTH_EVENT = "birth";
    private static final String MARRIAGE_EVENT = "marriage";
    private static final String DEATH_EVENT = "death";
    private static final String ASTEROIDS1_EVENT_ID = "Sheila_Asteroids";
    private static final String ASTEROIDS2_EVENT_ID = "Other_Asteroids";
    private static final String INDEX_HTML_PATH = "web/index.html";
    private static final String PAGE_NOT_FOUND_HTML_PATH = "web/HTML/404.html";
    private static final String MAIN_CSS_PATH = "web/css/main.css";
    private static final int MIN_REALISTIC_MARRIAGE_AGE = 13;
    private static final int MIN_REALISTIC_PREGNANT_AGE = 13;
    private static final int MAX_REALISTIC_PREGNANT_AGE = 50;
    private static final int MAX_REALISTIC_DEATH_AGE = 120;
    private static final String EMPTY_STRING = "";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static String host = "localhost";
    private static String port = "8080";
    private static boolean displayCurrentTest = true;
    private Proxy proxy;

    public static void setHost(String host) {
        ServerTest.host = host;
    }

    public static void setPort(String port) {
        ServerTest.port = port;
    }

    public static void setDisplayCurrentTest(boolean displayCurrentTest) {
        ServerTest.displayCurrentTest = displayCurrentTest;
    }

    /**
     * Attempts to make a connection to your server that you wrote
     */
    @BeforeEach
    @DisplayName("Setup")
    public void setup(TestInfo testInfo) throws ServerConnectionException {
        logger.info("Setting up " + testInfo.getDisplayName() + "...");
        proxy = new Proxy();
        /*
            If a test fail on line 91 (the one following this comment),
            make sure the host and port variables a couple dozen lines
            up are set to the correct parameters.
        */
        proxy.clear(host, port);
    }

    /**
     * Required API calls:
     * Register
     */
    @Test
    @DisplayName("Register Valid New User Test")
    public void testValidNewRegister(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult result = proxy.register(host, port, registerRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if registerResult has an authtoken String
            assertNotNull(result.getAuthtoken(), "authtoken was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled registerResult with an authtoken String
            assertNotEquals(EMPTY_STRING, result.getAuthtoken(), "authtoken was empty string, expected non-empty authtoken string");
            //Checks to see if registerResult has a personID String
            assertNotNull(result.getPersonID(), "personID was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled registerResult with a personID String
            assertNotEquals(EMPTY_STRING, result.getPersonID(), "personID was empty string, expected non-empty string containing the personID of the user's generated Person object");
            //Checks to see if registerResult has a username String
            assertNotNull(result.getUsername(), "username was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled registerResult with a username String
            assertNotEquals(EMPTY_STRING, result.getUsername(), "username was empty string, expected username passed in with passoffrequest");
            //Checks to see if the username string matches the one that was in the request (sheila)
            assertEquals(SHEILA.getUsername(), result.getUsername(), "username from Server does not match the requested username");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     */
    @Test
    @DisplayName("Re-Register User Test")
    public void testReRegister(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //Checks to see if registerResult has an authtoken String
            assertNotNull(registerResult.getAuthtoken(), "authtoken was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled registerResult with an authtoken String
            assertNotEquals(EMPTY_STRING, registerResult.getAuthtoken(), "authtoken was empty string, expected non-empty authtoken string");
            //We are calling the register api for a user named sheila
            registerResult = proxy.register(host, port, registerRequest);
            //Registering the same username is invalid, so the headers sent should be HTTP_BAD_REQUEST (400)
            assertHTTP_BAD_REQUEST();
            //registerResult should be invalid, so authtoken should be null
            assertNull(registerResult.getAuthtoken(), "authtoken was not null when it should have been (see API)");
            //registerResult should be invalid, so personID should be null
            assertNull(registerResult.getPersonID(), "personID was not null when it should have been (see API)");
            //registerResult should be invalid, so the message variable should contain a non-null error string
            assertNotNull(registerResult.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Non-null error strings should include the word "error"
            assertTrue(registerResult.getMessage().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Login
     */
    @Test
    @DisplayName("Login Valid User Test")
    public void testValidUserLogin(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the login api for a user named sheila
            LoginResult result = proxy.login(host, port, loginRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if loginResult has an authtoken String
            assertNotNull(result.getAuthtoken(), "authtoken was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loginResult with an authtoken String
            assertNotEquals(EMPTY_STRING, result.getAuthtoken(), "authtoken was empty string, expected non-empty authtoken string");
            //Checks to see if loginResult has a personID String
            assertNotNull(result.getPersonID(), "personID was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loginResult with a personID String
            assertNotEquals(EMPTY_STRING, result.getPersonID(), "personID was empty string, expected non-empty string containing the personID of the user's generated Person object");
            //Checks to see if loginResult and registerResult have the same generated personID string
            assertEquals(registerResult.getPersonID(), result.getPersonID(), "personID does not match the personID that was returned from register");
            //Checks to see if loginResult has a username String
            assertNotNull(result.getUsername(), "username was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loginResult with a username String
            assertNotEquals(EMPTY_STRING, result.getUsername(), "username was empty string, expected username passed in with passoffrequest");
            //Checks to see if the username string from loginResult matches the username that was in both requests (sheila)
            assertEquals(SHEILA.getUsername(), result.getUsername(), "username from Server does not match the requested username ");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Login
     */
    @Test
    @DisplayName("Login Invalid User Test")
    public void testInvalidUserLogin(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            proxy.register(host, port, registerRequest);
            //We are calling the login api for a user named patrick
            LoginResult result = proxy.login(host, port, loginRequest2);
            //We just tried logging in with an invalid user, this checks to make sure loginResult reflects that
            assertFailedLogin(result);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Login
     */
    @Test
    @DisplayName("Login Invalid Password Test")
    public void testInvalidPasswordLogin(TestInfo testInfo) {
        printTestName(testInfo);
        //We create a loginRequest with an incorrect password for sheila
        LoginRequest loginRequest = new LoginRequest(SHEILA.getUsername(), PATRICK.getPassword());
        try {
            //We are calling the register api for a user named sheila
            proxy.register(host, port, registerRequest);
            //We are calling the login api for sheila using our bad request we created near the beginning of this test
            LoginResult result = proxy.login(host, port, loginRequest);
            //We just tried logging in with an invalid password, this checks to make sure loginResult reflects that
            assertFailedLogin(result);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Person/[personID]
     * (Partial) Fill
     */
    @Test
    @DisplayName("Person Valid Test")
    public void testValidPerson(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the get single person api using the personID and authtoken variable from registerResult on the previous line
            PersonResult personResult = proxy.person(host, port, registerResult.getAuthtoken(), registerResult.getPersonID());
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to make sure the personResult has a personID that matches the personID requested (using the personID variable from the RegisterResult we got near the beginning of this test)
            assertEquals(registerResult.getPersonID(), personResult.getPersonID(), "personID returned doesn't match personID asked for");
            //Checks to make sure the personResult has a firstName that matches the firstName requested ("Sheila")
            assertEquals(registerRequest.getFirstName(), personResult.getFirstName(), "firstName of person returned does not match that of user's registration");
            //Checks to make sure the personResult has a lastName that matches the lastName requested ("Parker")
            assertEquals(registerRequest.getLastName(), personResult.getLastName(), "lastName of person returned does not match that of user's registration");
            //Checks to make sure the personResult has a gender that matches the gender requested ("f")
            assertEquals(registerRequest.getGender(), personResult.getGender(), "gender of person returned does not match that of user's registration");
            //Checks to make sure the personResult has an associatedUsername that matches the username requested ("sheila")
            assertEquals(registerResult.getUsername(), personResult.getAssociatedUsername(), "username of person returned does not match that of user's registration");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Load
     * Login
     * Person/[personID]
     */
    @Test
    @DisplayName("Person Wrong User Test")
    public void testWrongUserPerson(TestInfo testInfo) {
        printTestName(testInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are calling the login api for a user named sheila
            LoginResult loginResult = proxy.login(host, port, loginRequest);
            //We are calling the get single person api with a personID of "Patrick_Spencer" (we are using the authtoken variable from loginResult on the previous line)
            //"Patrick_Spencer" is a person from a different family tree!
            PersonResult personResult = proxy.person(host, port, loginResult.getAuthtoken(), PATRICK.getPersonID());
            //Trying to access a person that is not related to you is invalid, so the headers sent should be HTTP_BAD_REQUEST (400)
            assertHTTP_BAD_REQUEST();
            //personResult should be invalid, so gender should be null
            assertNull(personResult.getGender(), "gender of invalidly requested person was given");
            //personResult should be invalid, so motherID should be null
            assertNull(personResult.getMotherID(), "motherID of invalidly requested person was given");
            //personResult should be invalid, so fatherID should be null
            assertNull(personResult.getFatherID(), "fatherID of invalidly requested person was given");
            //personResult should be invalid, so spouseID should be null
            assertNull(personResult.getSpouse(), "spouseID of invalidly requested person was given");
            //personResult should be invalid, so username should be null
            assertNull(personResult.getAssociatedUsername(), "username of invalidly requested person was given");
            //personResult should be invalid, so firstName should be null
            assertNull(personResult.getFirstName(), "firstName of invalidly requested person was given");
            //personResult should be invalid, so lastName should be null
            assertNull(personResult.getLastName(), "lastName of invalidly requested person was given");
            //personResult should be invalid, so the message variable should contain a non-null error string
            assertNotNull(personResult.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Non-null error strings should not be left empty
            assertNotEquals(EMPTY_STRING, personResult.getMessage(), "message was empty string, should have contained an error message");
            //Non-null error strings should include the word "error"
            assertTrue(personResult.getMessage().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * (Partial) Person/[personID]
     */
    @Test
    @DisplayName("Person Bad Auth Token Test")
    public void testBadAuthTokenPerson(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the get single person api using the personID variable from registerResult on the previous line
            //However, here we are using an authtoken that is not registered with this user
            PersonResult personResult = proxy.person(host, port, "bad auth", registerResult.getPersonID());
            //Trying to access a person with an unregistered authtoken is invalid, so the headers sent should be HTTP_BAD_REQUEST (400)
            assertHTTP_BAD_REQUEST();
            //registerResult should be invalid, so the message variable should contain a non-null error string
            assertNotNull(personResult.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Non-null error strings should not be left empty
            assertNotEquals(EMPTY_STRING, personResult.getMessage(), "message was empty string, should have contained an error message");
            //Non-null error strings should include the word "error"
            assertTrue(personResult.getMessage().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * (Partial) All Person
     * (Partial) Fill
     */
    @Test
    @DisplayName("Persons Valid Test")
    public void testValidPersons(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authtoken variable from registerResult on the previous line)
            PersonsResult personsResult = proxy.persons(host, port, registerResult.getAuthtoken());
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Check to see that sheila's person information is in the list of people inside the personsResult
            assertNotNull(personsResult.getPerson(registerResult.getPersonID()), "User's person not found in passoffresult");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * (Partial) All Person
     */
    @Test
    @DisplayName("Persons Bad Auth Token Test")
    public void testBadAuthTokenPersons(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            proxy.register(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila
            //However, here we are using an authtoken that is not registered with this user
            PersonsResult personsResult = proxy.persons(host, port, "bad auth");
            //We just tried accessing sheila's family tree with an invalid authtoken, this checks to make sure personsResult reflects that
            assertFailedPersons(personsResult);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Load
     * Login
     * Event/[eventID]
     */
    @Test
    @DisplayName("Event Valid Test")
    public void testValidEvent(TestInfo testInfo) {
        printTestName(testInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            //We are calling the login api for a user named sheila
            LoginResult loginResult = proxy.login(host, port, loginRequest);
            //We are calling the get single event api with an eventID of "Sheila_Asteroids" (we are using the authtoken variable from loginResult on the previous line)
            EventResult eventResult = proxy.event(host, port, loginResult.getAuthtoken(), ASTEROIDS1_EVENT_ID);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to make sure eventResult has information for an Event [OR] in the case that there isn't, that there is no error message String
            Assertions.assertTrue(eventResult.getMessage() == null || !eventResult.getMessage().toLowerCase().contains("error"), "Result contains an error message");
            //Checks to make sure eventResult has the same information for the "Sheila_Asteroids" Event as it is listed in loadRequest
            assertEquals(loadRequest.getEvent(ASTEROIDS1_EVENT_ID), eventResult.toEvent(), "Event returned does not match event from LoadRequest");
            //We are calling the get single event api with an eventID of "Other_Asteroids" (we are using the authtoken variable from the same loginResult)
            eventResult = proxy.event(host, port, loginResult.getAuthtoken(), ASTEROIDS2_EVENT_ID);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to make sure eventResult has information for an Event [OR] in the case that there isn't, that there is no error message String
            Assertions.assertTrue(eventResult.getMessage() == null || !eventResult.getMessage().toLowerCase().contains("error"), "Result contains an error message");
            //Checks to make sure eventResult has the same information for the "Other_Asteroids" Event as it is listed in loadRequest
            assertEquals(loadRequest.getEvent(ASTEROIDS2_EVENT_ID), eventResult.toEvent(), "Event returned does not match event from LoadRequest");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }

    /**
     * Required API calls:
     * Load
     * (Partial) Event/[eventID]
     */
    @Test
    @DisplayName("Event Bad Auth Token Test")
    public void testBadAuthTokenEvent(TestInfo testInfo) {
        printTestName(testInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are calling the get single event api using the eventID variable "Sheila_Asteroids"
            //However, here we are using an authtoken that is not registered with this user
            EventResult eventResult = proxy.event(host, port, "bad auth", ASTEROIDS1_EVENT_ID);
            //Trying to access an event with an unregistered authtoken or eventID is invalid, so the headers sent should be HTTP_BAD_REQUEST (400)
            assertHTTP_BAD_REQUEST();
            //eventResult should be invalid, so the message variable should contain a non-null error string
            assertNotNull(eventResult.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Non-null error strings should not be left empty
            assertNotEquals(EMPTY_STRING, eventResult.getMessage(), "message was empty string, should have contained an error message");
            //Non-null error strings should include the word "error"
            assertTrue(eventResult.getMessage().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Load
     * Login
     * Event/[eventID]
     */
    @Test
    @DisplayName("Event Wrong User Test")
    public void testWrongUserEvent(TestInfo testInfo) {
        printTestName(testInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are calling the login api for a user named patrick
            LoginResult loginResult = proxy.login(host, port, loginRequest2);
            //We are calling the get single event api with an eventID of "Sheila_Asteroids" (we are using the authtoken variable from loginResult on the previous line)
            //"Patrick_Spencer" is a person from a different family tree!
            EventResult eventResult = proxy.event(host, port, loginResult.getAuthtoken(), ASTEROIDS1_EVENT_ID);
            //We just tried accessing an event from sheila's family tree with another user's authtoken
            //which is invalid, this checks to make sure eventResult reflects that
            assertFailedEvent(eventResult);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * (Partial) All Event
     * (Partial) Fill
     */
    @Test
    @DisplayName("Events Valid Test")
    public void testValidEvents(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the get all events api for the user sheila (we are using the authtoken variable from registerResult on the previous line)
            EventsResult eventsResult = proxy.events(host, port, registerResult.getAuthtoken());
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Check to see that sheila has an event with the eventType "birth" in the list of events inside the eventsResult
            assertNotNull(eventsResult.getEvent(registerResult.getPersonID(), BIRTH_EVENT), "Result does not contain User's birth");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * (Partial) All Events
     */
    @Test
    @DisplayName("Events Bad Auth Token Test")
    public void testBadAuthTokenEvents(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            proxy.register(host, port, registerRequest);
            //We are calling the get all events api for the user sheila
            //However, here we are using an authtoken that is not registered with this user
            EventsResult eventsResult = proxy.events(host, port, "bad auth");
            //We just tried accessing sheila's family tree of events with an invalid authtoken, this checks to make sure personsResult reflects that
            assertFailedEvents(eventsResult);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * All Person
     * Fill
     */
    @Test
    @DisplayName("Valid Fill Relationships Test")
    public void testValidFillRelationships(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authtoken variable from registerResult on the previous line)
            PersonsResult personsResult = proxy.persons(host, port, registerResult.getAuthtoken());
            //Here we are getting the Person information for the user sheila
            Person userPerson = personsResult.getPerson(registerResult.getPersonID());
            //Checks to make sure the right amount of people were added to the database after the register service
            checkPersonsParents(personsResult, userPerson, "User", 3);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * All Person
     * All Event
     * Fill
     */
    @Test
    @DisplayName("Realistic Fill Birth Test")
    public void testRealisticBirthEvents(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authtoken variable from registerResult on the previous line)
            PersonsResult personsResult = proxy.persons(host, port, registerResult.getAuthtoken());
            //We are calling the get all events api for the user sheila (we are using the authtoken variable from the same registerResult)
            EventsResult eventsResult = proxy.events(host, port, registerResult.getAuthtoken());
            //Here we are getting the Person information for the user sheila
            Person userPerson = personsResult.getPerson(registerResult.getPersonID());
            //If person is null then a Person Object was not created for the user and inserted into the database
            assertNotNull(userPerson, "User's Person not included in passoffresult");
            //Checks for all the required birth events and makes sure the years make sense
            checkPersonsBirth(eventsResult, personsResult, userPerson, "User", 3);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * All Person
     * All Event
     * Fill
     */
    @Test
    @DisplayName("Realistic Fill Death Test")
    public void testRealisticDeathEvents(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authtoken variable from registerResult on the previous line)
            PersonsResult personsResult = proxy.persons(host, port, registerResult.getAuthtoken());
            //We are calling the get all events api for the user sheila (we are using the authtoken variable from the same registerResult)
            EventsResult eventsResult = proxy.events(host, port, registerResult.getAuthtoken());
            //Here we are getting the Person information for the user sheila
            Person userPerson = personsResult.getPerson(registerResult.getPersonID());
            //If person1 is null then a Person Object was not created for the user and inserted into the database
            assertNotNull(userPerson, "User's Person not included in passoffresult");
            //Here we are getting the Person information for sheila's father
            Person userFather = personsResult.getPerson(userPerson.getFatherID());
            //Here we are getting the Person information for sheila's mother
            Person userMother = personsResult.getPerson(userPerson.getMotherID());
            //If person2 is null then a Person Object was not created for the user's father and inserted into the database
            assertNotNull(userFather, "User's Father's Person not included in passoffresult");
            //If person3 is null then a Person Object was not created for the user's mother and inserted into the database
            assertNotNull(userMother, "User's Mother's Person not included in passoffresult");
            //Checks for all the required death events for the user's mother's side and makes sure the years make sense
            checkPersonsDeath(eventsResult, personsResult, userMother, "User's mother", 3);
            //Checks for all the required death events for the user's father's side and makes sure the years make sense
            checkPersonsDeath(eventsResult, personsResult, userFather, "User's father", 3);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * All Person
     * All Event
     * Fill
     */
    @Test
    @DisplayName("Realistic Fill Marriage Test")
    public void testRealisticFillMarriage(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = proxy.register(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authtoken variable from registerResult on the previous line)
            PersonsResult personsResult = proxy.persons(host, port, registerResult.getAuthtoken());
            //We are calling the get all events api for the user sheila (we are using the authtoken variable from the same registerResult)
            EventsResult eventsResult = proxy.events(host, port, registerResult.getAuthtoken());
            //Here we are getting the Person information for the user sheila
            Person userPerson = personsResult.getPerson(registerResult.getPersonID());
            //If person is null then a Person Object was not created for the user and inserted into the database
            assertNotNull(userPerson, "User's Person not included in passoffresult");
            //Checks for all the required marriage events for all children's parents, ensures the years make sense,
            //and that the marriage is in the same location/year for each couple.
            checkParentsMarriage(eventsResult, personsResult, userPerson, "User", 2);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Load
     * Fill
     * Login
     * All Person
     * All Event
     */
    @Test
    @DisplayName("Fill Does Not Affect Other Users Test")
    public void testFillDoesNotAffectOtherUsers(TestInfo testInfo) {
        printTestName(testInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        //We are creating a fill request with the username sheila and the generations as 4
        int generations = 4;
        FillRequest fillRequest = new FillRequest(SHEILA.getUsername(), generations);
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            //We are calling the fill api using the fillRequest we created near the beginning of this test
            proxy.fill(host, port, fillRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //We are calling the login api for a user named patrick
            LoginResult loginResult = proxy.login(host, port, loginRequest2);
            //Checks to see if loginResult has an authtoken String
            assertNotNull(loginResult.getAuthtoken(), "authtoken was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loginResult with an authtoken String
            assertNotEquals(EMPTY_STRING, loginResult.getAuthtoken(), "authtoken was empty string, expected non-empty authtoken string");
            //We are calling the get all persons api for the user patrick (we are using the authtoken variable from the LoginResult we received a few lines up)
            PersonsResult personsResult = proxy.persons(host, port, loginResult.getAuthtoken());
            //Checks to see if the list of people associated with patrick from loadRequest matches the list of people from personsResult
            assertEquals(loadRequest.getPersons(PATRICK.getUsername()), personsResult.getDataAsSet(), "Persons of one user don't match loaded persons after a fill of a different user");
            //We are calling the get all events api for the user patrick (we are using the authtoken variable from the same loginResult)
            EventsResult eventsResult = proxy.events(host, port, loginResult.getAuthtoken());
            //Checks to see if the list of events associated with patrick from loadRequest matches the list of events from eventsResult
            assertEquals(loadRequest.getEvents(PATRICK.getUsername()), eventsResult.getDataAsSet(), "Events of one user don't match loaded events after a fill of a different user");
        } catch (ServerConnectionException | FileNotFoundException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Fill
     */
    @Test
    @DisplayName("Fill 4 Valid Test")
    public void testValidFill4(TestInfo testInfo) {
        printTestName(testInfo);
        //We are creating a fill request with the username sheila and the generations as 4
        int generations = 4;
        FillRequest fillRequest = new FillRequest(SHEILA.getUsername(), generations);
        int minimumPeople = (int) Math.pow(2, generations + 1) - 1;
        int minEvents = minimumPeople * 2;
        try {
            //We are calling the register api for a user named sheila
            proxy.register(host, port, registerRequest);
            //We are calling the fill api using the fillRequest we created near the beginning of this test
            FillResult result = proxy.fill(host, port, fillRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if fillResult has a message String
            assertNotNull(result.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled fillResult with a message String
            assertNotEquals(EMPTY_STRING, result.getMessage(), "message was empty string");
            //Splits the fillResult message into four crucial parts
            String[] message = result.getMessage().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            //Checks to be sure the fillResult message starts with the phrase "Successfully added "
            Assertions.assertEquals("Successfully added ", message[0], "First part of passoffresult message does not match API");
            //Checks to be sure the fillResult message confirms that 31 people were added into the database
            Assertions.assertTrue(minimumPeople <= Integer.parseInt(message[1]), "Not enough people added");
            //Checks to be sure the fillResult message has the phrase " persons and " in between listing the number of people and the number of events
            Assertions.assertEquals(" persons and ", message[2], "Second part of passoffresult message does not match API");
            //Checks to be sure the fillResult message confirms that 91 events were added into the database
            Assertions.assertTrue(minEvents <= Integer.parseInt(message[3]), "Not enough events added");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Fill
     */
    @Test
    @DisplayName("Fill 2 Valid Test")
    public void testValidFill2(TestInfo testInfo) {
        printTestName(testInfo);
        //We are creating a fill request with the username sheila and the generations as 2
        int generations = 2;
        FillRequest fillRequest = new FillRequest(SHEILA.getUsername(), generations);
        int minimumPeople = (int) Math.pow(2, generations + 1) - 1;
        int minEvents = minimumPeople * 2;
        try {
            //We are calling the register api for a user named sheila
            proxy.register(host, port, registerRequest);
            //We are calling the fill api using the fillRequest we created near the beginning of this test
            FillResult result = proxy.fill(host, port, fillRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if fillResult has a message String
            assertNotNull(result.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled fillResult with a message String
            assertNotEquals(EMPTY_STRING, result.getMessage(), "message was empty string");
            //Splits the fillResult message into four crucial parts
            String[] message = result.getMessage().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            //Checks to be sure the fillResult message starts with the phrase "Successfully added "
            Assertions.assertEquals("Successfully added ", message[0], "First part of passoffresult message does not match API");
            //Checks to be sure the fillResult message confirms that 7 people were added into the database
            Assertions.assertTrue(minimumPeople <= Integer.parseInt(message[1]), "Not enough people added");
            //Checks to be sure the fillResult message has the phrase " persons and " in between listing the number of people and the number of events
            Assertions.assertEquals(" persons and ", message[2], "Second part of passoffresult message does not match API");
            //Checks to be sure the fillResult message confirms that 19 events were added into the database
            Assertions.assertTrue(minEvents <= Integer.parseInt(message[3]), "Not enough events added");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Fill
     */
    @Test
    @DisplayName("Fill 5 Valid Test")
    public void testValidFill5(TestInfo testInfo) {
        printTestName(testInfo);
        //We are creating a fill request with the username sheila and the generations as 5
        int generations = 5;
        FillRequest fillRequest = new FillRequest(SHEILA.getUsername(), generations);
        int minimumPeople = (int) Math.pow(2, generations + 1) - 1;
        int minEvents = minimumPeople * 2;
        try {
            //We are calling the register api for a user named sheila
            proxy.register(host, port, registerRequest);
            //We are calling the fill api using the fillRequest we created near the beginning of this test
            FillResult result = proxy.fill(host, port, fillRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if fillResult has a message String
            assertNotNull(result.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled fillResult with a message String
            assertNotEquals(EMPTY_STRING, result.getMessage(), "message was empty string");
            //Splits the fillResult message into four crucial parts
            String[] message = result.getMessage().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            //Checks to be sure the fillResult message starts with the phrase "Successfully added "
            Assertions.assertEquals("Successfully added ", message[0], "First part of passoffresult message does not match API");
            //Checks to be sure the fillResult message confirms that 63 people were added into the database
            Assertions.assertTrue(minimumPeople <= Integer.parseInt(message[1]), "Not enough people added");
            //Checks to be sure the fillResult message has the phrase " persons and " in between listing the number of people and the number of events
            Assertions.assertEquals(" persons and ", message[2], "Second part of passoffresult message does not match API");
            //Checks to be sure the fillResult message confirms that 187 events were added into the database
            Assertions.assertTrue(minEvents <= Integer.parseInt(message[3]), "Not enough events added");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Load
     */
    @Test
    @DisplayName("Load Valid Test")
    public void testValidLoad(TestInfo testInfo) {
        printTestName(testInfo);
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            int users = loadRequest.getUsers().size();
            int persons = loadRequest.getPersons().size();
            int events = loadRequest.getEvents().size();
            //We are calling the register api for a user named sheila
            proxy.register(host, port, registerRequest);
            //We are calling the load api using the loadRequest we created near the beginning of this test
            LoadResult result = proxy.load(host, port, loadRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if loadResult has a message String
            assertNotNull(result.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loadResult with a message String
            assertNotEquals(EMPTY_STRING, result.getMessage(), "message was empty string");
            //Non-null, non-error strings should not include the word "error"
            assertFalse(result.getMessage().toLowerCase().contains("error"), "message contained an error");
            //Splits the loadResult message into six crucial parts
            String[] message = result.getMessage().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            //Checks to be sure the loadResult message starts with the phrase "Successfully added "
            Assertions.assertEquals("Successfully added ", message[0], "First part of passoffresult message does not match API");
            //Checks to be sure the loadResult message confirms that 2 users were added into the database
            Assertions.assertEquals(users, Integer.parseInt(message[1]), "Incorrect number of users added");
            //Checks to be sure the loadResult message has the phrase " users, " in between listing the number of users and the number of persons
            Assertions.assertEquals(" users, ", message[2], "Second part of passoffresult message does not match API");
            //Checks to be sure the loadResult message confirms that 11 people were added into the database
            Assertions.assertEquals(persons, Integer.parseInt(message[3]), "Incorrect number of persons added");
            //Checks to be sure the loadResult message has the phrase " persons, and " in between listing the number of persons and the number of events
            Assertions.assertEquals(" persons, and ", message[4], "Third part of passoffresult message does not match API");
            //Checks to be sure the loadResult message confirms that 19 events were added into the database
            Assertions.assertEquals(events, Integer.parseInt(message[5]), "Incorrect number of events added");
        } catch (FileNotFoundException e) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Load
     * Login
     * All Event
     * All Person
     */
    @Test
    @DisplayName("Load Valid Info Test")
    public void testValidLoadInfo(TestInfo testInfo) {
        printTestName(testInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            //We are calling the login api for a user named sheila
            LoginResult loginResult = proxy.login(host, port, loginRequest);
            //We are calling the get all events api for the user sheila (we are using the authtoken variable from loginResult on the previous line)
            EventsResult eventsResult = proxy.events(host, port, loginResult.getAuthtoken());
            //We are calling the get all persons api for the user sheila (we are using the authtoken variable from the same loginResult)
            PersonsResult personsResult = proxy.persons(host, port, loginResult.getAuthtoken());
            //Checks to see if the list of events associated with sheila from loadRequest matches the list of events from eventsResult
            assertEquals(loadRequest.getEvents(loginRequest.getUsername()), eventsResult.getDataAsSet(), SHEILA.getUsername() + "'s events do not match those loaded");
            //Checks to see if the list of people associated with sheila from loadRequest matches the list of people from personsResult
            assertEquals(loadRequest.getPersons(loginRequest.getUsername()), personsResult.getDataAsSet(), SHEILA.getUsername() + "'s persons do not match those loaded");
            //We are calling the login api for a user named patrick
            loginResult = proxy.login(host, port, loginRequest2);
            //We are calling the get all events api for the user patrick (we are using the authtoken variable from loginResult on the previous line)
            eventsResult = proxy.events(host, port, loginResult.getAuthtoken());
            //We are calling the get all persons api for the user patrick (we are using the authtoken variable from the same loginResult)
            personsResult = proxy.persons(host, port, loginResult.getAuthtoken());
            //Checks to see if the list of events associated with patrick from loadRequest matches the list of events from eventsResult
            assertEquals(loadRequest.getEvents(loginRequest2.getUsername()), eventsResult.getDataAsSet(), PATRICK.getUsername() + "'s events do not match those loaded");
            //Checks to see if the list of people associated with patrick from loadRequest matches the list of people from personsResult
            assertEquals(loadRequest.getPersons(loginRequest2.getUsername()), personsResult.getDataAsSet(), PATRICK.getUsername() + "'s persons do not match those loaded");
        } catch (FileNotFoundException e) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * Load
     * Login
     * All Event
     * All Person
     */
    @Test
    @DisplayName("Persistence Test")
    public void testPersistence(TestInfo testInfo) {
        printTestName(testInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Shut down the server, wait a few seconds, then restart the server. Then press ENTER.");
        //Will wait on the statement scanner.nextLine() till you push enter in the terminal window.
        //You may need to follow the steps under the heading "Setting up for the Persistence Test" in the "How To Get Started"
        //tutorial, linked in canvas, in order to get this to work properly. The "How to Get Started" tutorial can be found
        //inside the Family Map Server Program assignment listed on the Assignments tab on canvas.
        scanner.nextLine();
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            //We are calling the login api for a user named sheila
            LoginResult loginResult = proxy.login(host, port, loginRequest);
            //We are calling the get all events api for the user sheila (we are using the authtoken variable from loginResult on the previous line)
            EventsResult eventsResult = proxy.events(host, port, loginResult.getAuthtoken());
            //We are calling the get all people api for the user sheila (we are using the authtoken variable from the same loginResult)
            PersonsResult personsResult = proxy.persons(host, port, loginResult.getAuthtoken());
            //Checks to see if the list of events associated with sheila from loadRequest matches the list of events from eventsResult
            assertEquals(loadRequest.getEvents(loginRequest.getUsername()), eventsResult.getDataAsSet(), SHEILA.getUsername() + "'s events do not match those loaded");
            //Checks to see if the list of people associated with sheila from loadRequest matches the list of people from personsResult
            assertEquals(loadRequest.getPersons(loginRequest.getUsername()), personsResult.getDataAsSet(), SHEILA.getUsername() + "'s persons do not match those loaded");
            //We are calling the login api for a user named patrick
            loginResult = proxy.login(host, port, loginRequest2);
            //We are calling the get all events api for the user patrick (we are using the authtoken variable from loginResult on the previous line)
            eventsResult = proxy.events(host, port, loginResult.getAuthtoken());
            //We are calling the get all people api for the user patrick (we are using the authtoken variable from the same loginResult)
            personsResult = proxy.persons(host, port, loginResult.getAuthtoken());
            //Checks to see if the list of events associated with patrick from loadRequest matches the list of events from eventsResult
            assertEquals(loadRequest.getEvents(loginRequest2.getUsername()), eventsResult.getDataAsSet(), PATRICK.getUsername() + "'s events do not match those loaded");
            //Checks to see if the list of people associated with patrick from loadRequest matches the list of people from personsResult
            assertEquals(loadRequest.getPersons(loginRequest2.getUsername()), personsResult.getDataAsSet(), PATRICK.getUsername() + "'s persons do not match those loaded");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }

    /**
     * Required API calls:
     * Load
     * Login
     * Clear
     * (Partial) All Person
     * (Partial) All Event
     */
    @Test
    @DisplayName("Clear Test")
    public void testClear(TestInfo testInfo) {
        printTestName(testInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are calling the login api for a user named sheila
            LoginResult oldLoginResult = proxy.login(host, port, loginRequest);
            //We are calling the clear api
            ClearResult clearResult = proxy.clear(host, port);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if clearResult has a message String
            assertNotNull(clearResult.getMessage(), "Clear message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled clearResult with a message String
            assertNotEquals(EMPTY_STRING, clearResult.getMessage(), "Clear message was empty string");
            //Checks to be sure the clearResult message contains the words "clear succeeded"
            assertTrue(clearResult.getMessage().toLowerCase().contains("clear succeeded"), "Clear message did not contain the APIs success message");
            //We are calling the login api for a user named sheila
            LoginResult loginResult = proxy.login(host, port, loginRequest);
            //The database is empty so trying to login at all is invalid, this checks to make sure loginResult2 reflects that
            assertFailedLogin(loginResult);
            //We are calling the login api for a user named patrick
            loginResult = proxy.login(host, port, loginRequest2);
            //The database is empty so trying to login at all is invalid, this checks to make sure loginResult2 reflects that
            assertFailedLogin(loginResult);
            //We are calling the get all people api for the user sheila (we are using the authtoken variable from loginResult1 near the beginning of this test)
            PersonsResult personsResult = proxy.persons(host, port, oldLoginResult.getAuthtoken());
            //The database is empty so trying to get a list of people is invalid, this checks to make sure personsResult reflects that
            assertFailedPersons(personsResult);
            //We are calling the get all events api for the user sheila (we are using the authtoken variable from loginResult1 near the beginning of this test)
            EventsResult eventsResult = proxy.events(host, port, oldLoginResult.getAuthtoken());
            //The database is empty so trying to get a list of events is invalid, this checks to make sure eventsResult reflects that
            assertFailedEvents(eventsResult);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * File Handler
     */
    @Test
    @DisplayName("File Handler Default Test")
    public void testFileHandlerDefault(TestInfo testInfo) {
        printTestName(testInfo);
        String indexHTMLStr = fileToString(INDEX_HTML_PATH);
        assert indexHTMLStr != null;
        indexHTMLStr = indexHTMLStr.replaceAll("\r", ""); //ignore new lines
        indexHTMLStr = indexHTMLStr.replaceAll("\n", ""); //ignore new lines
        try {
            //We are calling the File Handler api with a url of "" (empty string)
            String indexFromServer = proxy.file(host, port, EMPTY_STRING);
            indexFromServer = indexFromServer.replaceAll("\r", ""); //ignore new lines
            indexFromServer = indexFromServer.replaceAll("\n", ""); //ignore new lines
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if the File Handler gave a response
            //str1 holds the response the File Handler gave
            assertNotNull(indexFromServer);
            //Checks to see if the File Handler filled str1 with a String
            assertNotEquals(EMPTY_STRING, indexFromServer, "Default File Handler returned an empty file");
            //Checks to be sure str1 returned a path to your index.html file
            assertEquals(indexHTMLStr, indexFromServer, "Default File Handler did not return correct file (index.html), or file contents do not exactly match provided file");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * File Handler
     */
    @Test
    @DisplayName("File Handler Test")
    public void testFileHandler(TestInfo testInfo) {
        printTestName(testInfo);

        String mainCssStr = fileToString(MAIN_CSS_PATH);
        assert mainCssStr != null;
        mainCssStr = mainCssStr.replaceAll("\r", ""); //ignore new lines
        mainCssStr = mainCssStr.replaceAll("\n", ""); //ignore new lines

        try {
            //We are calling the File Handler api with a url of "css/main.css"
            String mainCssFromServer = proxy.file(host, port, "css/main.css");
            mainCssFromServer = mainCssFromServer.replaceAll("\r", ""); //ignore new lines
            mainCssFromServer = mainCssFromServer.replaceAll("\n", ""); //ignore new lines
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if the File Handler gave a response
            //str1 holds the response the File Handler gave
            assertNotNull(mainCssFromServer);
            //Checks to see if the File Handler filled str1 with a String
            assertNotEquals(EMPTY_STRING, mainCssFromServer, "File Handler returned an empty file");
            //Checks to be sure str1 returned a path to your main.css file
            assertEquals(mainCssStr, mainCssFromServer, "File Handler did not return correct file, or file contents do not exactly match provided file");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Required API calls:
     * File Handler
     */
    @Test
    @DisplayName("File Handler 404 Test")
    public void testFileHandler404(TestInfo testInfo) {
        printTestName(testInfo);

        String pageNotFoundHTMLStr = fileToString(PAGE_NOT_FOUND_HTML_PATH);
        assert pageNotFoundHTMLStr != null;
        pageNotFoundHTMLStr = pageNotFoundHTMLStr.replaceAll("\r", ""); //ignore new lines
        pageNotFoundHTMLStr = pageNotFoundHTMLStr.replaceAll("\n", ""); //ignore new lines

        try {
            //We are calling the File Handler api with a url of "junkExtension"
            String pageNotFoundFromServer = proxy.file(host, port, "junkExtension");
            pageNotFoundFromServer = pageNotFoundFromServer.replaceAll("\r", ""); //ignore new lines
            pageNotFoundFromServer = pageNotFoundFromServer.replaceAll("\n", ""); //ignore new lines
            //We gave a path to a file that doesn't exist in your project, so the headers sent should be HTTP_NOT_FOUND (404)
            assertEquals(HttpURLConnection.HTTP_NOT_FOUND, Client.getLastResponseCode(), "Response code from server was not HTTP_NOT_FOUND");
            //Checks to see if the File Handler gave a response
            //pageNotFoundFromServer holds the response the File Handler gave
            assertNotNull(pageNotFoundFromServer, "File Handler returned nothing");
            //Checks to see if the File Handler filled pageNotFoundFromServer with a String
            assertNotEquals(EMPTY_STRING, pageNotFoundFromServer, "File Handler returned an empty file");
            //Checks to be sure pageNotFoundFromServer returned a path to your 404.html file
            assertEquals(pageNotFoundHTMLStr, pageNotFoundFromServer, "File Handler did not return correct file, or file contents do not exactly match provided file");
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Takes a path to a file and returns its contents. Reports errors
     * if the file cannot be found or if there are errors while reading.
     *
     * @param filename path to file
     * @return human readable string
     */
    private String fileToString(String filename) {
        try {
            FileInputStream indexHTMLIndex = new FileInputStream(new File(filename));
            return readString(indexHTMLIndex);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assertions.fail("Failed to open " + filename + ". Place it in <project dir>/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Failed to read " + filename + ". Be sure that you have read access to " + filename);
        }
        return null;
    }

    /**
     * First checks to make sure the test that called this function received an
     * HTTP_BAD_REQUEST header. Then makes sure all the Event List is
     * empty. Ends by verifying that an error was reported.
     *
     * @param eventsResult A EventsResult holding an empty Event List and an error message
     */
    private void assertFailedEvents(EventsResult eventsResult) {
        assertHTTP_BAD_REQUEST();
        assertNull(eventsResult.getData(), "Events list was given when the auth token was bad");
        assertNotNull(eventsResult.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        assertNotEquals(EMPTY_STRING, eventsResult.getMessage(), "message was empty string, should have contained an error message");
        assertTrue(eventsResult.getMessage().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    /**
     * First checks to make sure the test that called this function received an
     * HTTP_BAD_REQUEST header. Then makes sure all the Person List is
     * empty. Ends by verifying that an error was reported.
     *
     * @param personsResult A PersonsResult holding an empty Person List and an error message
     */
    private void assertFailedPersons(PersonsResult personsResult) {
        assertHTTP_BAD_REQUEST();
        assertNull(personsResult.getData(), "Persons list was given when the auth token was bad");
        assertNotNull(personsResult.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        assertNotEquals(EMPTY_STRING, personsResult.getMessage(), "message was empty string, should have contained an error message");
        assertTrue(personsResult.getMessage().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    /**
     * First checks to make sure the test that called this function received an
     * HTTP_BAD_REQUEST header. Then makes sure all the data for a valid
     * loginResult is empty. Ends by verifying that an error was reported.
     *
     * @param loginResult A loginResult holding an error message
     */
    private void assertFailedLogin(LoginResult loginResult) {
        assertHTTP_BAD_REQUEST();
        assertNull(loginResult.getAuthtoken(), "authtoken was not null when it should have been (see API)");
        assertNull(loginResult.getPersonID(), "personID was not null when it should have been (see API)");
        assertNotNull(loginResult.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        assertTrue(loginResult.getMessage().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    /**
     * Uses the LoadData.json file to create information in the database which
     * the test that called this function can then use. It does this by sending
     * sending a Load API request to the student's server. The body of said
     * request would contain the information listed in LoadData.json.
     */
    private void load() {
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);

            proxy.load(host, port, loadRequest);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }

    /**
     * First checks to make sure the test that called this function received an
     * HTTP_BAD_REQUEST header. Then makes sure all the data for an Event is
     * empty. Ends by verifying that an error was reported.
     *
     * @param eventResult An EventResult holding an empty Event and an error message
     */
    private void assertFailedEvent(EventResult eventResult) {
        assertHTTP_BAD_REQUEST();
        assertNull(eventResult.getAssociatedUsername(), "username of invalidly requested event was given");
        assertNull(eventResult.getEventID(), "eventID of invalidly requested event was given");
        assertNull(eventResult.getPersonID(), "personID of invalidly requested event was given");
        assertNull(eventResult.getEventType(), "eventType of invalidly requested event was given");
        assertNull(eventResult.getCity(), "city of invalidly requested event was given");
        assertNull(eventResult.getCountry(), "country of invalidly requested event was given");

        assertNotNull(eventResult.getMessage(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        assertNotEquals(EMPTY_STRING, eventResult.getMessage(), "message was empty string, should have contained an error message");
        assertTrue(eventResult.getMessage().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    /**
     * Checks to make sure a couples marriage was included in an all events api call,
     * and that the marriage event of a couple are at a realistic age and at the same
     * time/place.
     * (This method is recursive, eventually looking at all the events in the family tree)
     *
     * @param eventsResult    List of Events
     * @param personsResult   List of People
     * @param person          Person we are looking at
     * @param relationship    Relation to the User
     * @param generationsLeft Number of generations
     */
    private void checkParentsMarriage(EventsResult eventsResult, PersonsResult personsResult, Person person, String relationship, int generationsLeft) {
        Person personFather = personsResult.getPerson(person.getFatherID());
        assertNotNull(personFather, relationship + "'s Father's Person not included in passoffresult");
        Event fatherBirth = eventsResult.getEvent(personFather.getPersonID(), BIRTH_EVENT);
        assertNotNull(fatherBirth, relationship + "'s Father's birth Event not included in passoffresult");
        int fatherBirthYear = fatherBirth.getYear();
        Event fatherMarriage = eventsResult.getEvent(personFather.getPersonID(), MARRIAGE_EVENT);
        assertNotNull(fatherMarriage, relationship + "'s Father's marriage Event not included in passoffresult");
        int fatherMarriageYear = fatherMarriage.getYear();
        Assertions.assertTrue(fatherMarriageYear - fatherBirthYear >= MIN_REALISTIC_MARRIAGE_AGE, relationship + "'s father was married unrealistically young, min marriage age: " + MIN_REALISTIC_MARRIAGE_AGE);

        Person personMother = personsResult.getPerson(person.getMotherID());
        assertNotNull(personMother, relationship + "'s Mother's Person not included in passoffresult");
        Event motherBirth = eventsResult.getEvent(personMother.getPersonID(), BIRTH_EVENT);
        assertNotNull(motherBirth, relationship + "'s Mother's birth Event not included in passoffresult");
        int motherBirthYear = motherBirth.getYear();
        Event motherMarriage = eventsResult.getEvent(personMother.getPersonID(), MARRIAGE_EVENT);
        assertNotNull(motherMarriage, relationship + "'s Mother's marriage Event not included in passoffresult");
        int motherMarriageYear = motherMarriage.getYear();
        Assertions.assertTrue(motherMarriageYear - motherBirthYear >= MIN_REALISTIC_MARRIAGE_AGE, relationship + "'s mother was married unrealistically young, min marriage age: " + MIN_REALISTIC_MARRIAGE_AGE);

        assertEquals(motherMarriage.getYear(), fatherMarriage.getYear(), relationship + "'s mother and father weren't married on the same day");
        assertEquals(motherMarriage.getCity(), fatherMarriage.getCity(), relationship + "'s mother and father weren't married in the same city");
        assertEquals(motherMarriage.getCountry(), fatherMarriage.getCountry(), relationship + "'s mother and father weren't married in the same country");

        if (generationsLeft > 0) {
            checkParentsMarriage(eventsResult, personsResult, personFather, relationship + "'s father", generationsLeft - 1);
            checkParentsMarriage(eventsResult, personsResult, personMother, relationship + "'s mother", generationsLeft - 1);
        }
    }

    /**
     * Checks to make sure a person's birth was included in an all events api call,
     * and that the birth event of a person's parents are at a realistic age.
     * (This method is recursive, eventually looking at all the events in the family tree)
     *
     * @param eventsResult    List of Events
     * @param personsResult   List of People
     * @param person          Person we are looking at
     * @param relationship    Relation to User
     * @param generationsLeft Number of generations
     */
    private void checkPersonsBirth(EventsResult eventsResult, PersonsResult personsResult, Person person, String relationship, int generationsLeft) {
        Event personBirth = eventsResult.getEvent(person.getPersonID(), BIRTH_EVENT);
        assertNotNull(personBirth, relationship + "'s birth Event not included in passoffresult");
        int personBirthYear = personBirth.getYear();

        Person personFather = personsResult.getPerson(person.getFatherID());
        assertNotNull(personFather, relationship + "'s Father's Person not included in passoffresult");
        Event fatherBirth = eventsResult.getEvent(personFather.getPersonID(), BIRTH_EVENT);
        assertNotNull(fatherBirth, relationship + "'s Father's birth Event not included in passoffresult");
        int fatherBirthYear = fatherBirth.getYear();
        int fatherAgeAtPersonBirth = personBirthYear - fatherBirthYear;
        Assertions.assertTrue(fatherAgeAtPersonBirth >= MIN_REALISTIC_PREGNANT_AGE, relationship + "'s father was unrealistically young at user's birth, min age of fatherhood: " + MIN_REALISTIC_PREGNANT_AGE);

        Person personMother = personsResult.getPerson(person.getMotherID());
        assertNotNull(personMother, relationship + "'s Mother's Person not included in passoffresult");
        Event motherBirth = eventsResult.getEvent(personMother.getPersonID(), BIRTH_EVENT);
        assertNotNull(motherBirth, relationship + "'s Mother's birth Event not included in passoffresult");
        int motherBirthYear = motherBirth.getYear();
        int motherAgeAtPersonBirth = personBirthYear - motherBirthYear;
        Assertions.assertTrue(motherAgeAtPersonBirth >= MIN_REALISTIC_PREGNANT_AGE, relationship + "'s mother was unrealistically young at user's birth, min pregnant age: " + MIN_REALISTIC_PREGNANT_AGE);
        Assertions.assertTrue(motherAgeAtPersonBirth <= MAX_REALISTIC_PREGNANT_AGE, relationship + "'s mother was unrealistically old at user's birth, max prenant age: " + MAX_REALISTIC_PREGNANT_AGE);

        if (generationsLeft > 0) {
            checkPersonsBirth(eventsResult, personsResult, personFather, relationship + "'s father", generationsLeft - 1);
            checkPersonsBirth(eventsResult, personsResult, personMother, relationship + "'s mother", generationsLeft - 1);
        }
    }

    /**
     * Checks to make sure a person's death was included in an all events api call,
     * and that the death event is at a realistic age.
     * (This method is recursive, eventually looking at all the events in the family tree)
     *
     * @param eventsResult    List of Events
     * @param personsResult   List of People
     * @param person          Person we are looking at
     * @param relationship    Relation to User
     * @param generationsLeft Number of generations
     */
    private void checkPersonsDeath(EventsResult eventsResult, PersonsResult personsResult, Person person, String relationship, int generationsLeft) {
        Event birth = eventsResult.getEvent(person.getPersonID(), BIRTH_EVENT);
        assertNotNull(birth, relationship + "'s birth Event not included in passoffresult");
        int birthYear = birth.getYear();
        Event death = eventsResult.getEvent(person.getPersonID(), DEATH_EVENT);
        assertNotNull(death, relationship + "'s death Event not included in passoffresult");
        int deathYear = death.getYear();
        int ageAtDeath = deathYear - birthYear;
        Assertions.assertTrue(ageAtDeath <= MAX_REALISTIC_DEATH_AGE, relationship + " was unrealistically old at his/her death, max death age: " + MAX_REALISTIC_DEATH_AGE);

        Person personMother = personsResult.getPerson(person.getMotherID());
        Person personFather = personsResult.getPerson(person.getFatherID());

        if (generationsLeft > 0) {
            assertNotNull(personMother, relationship + "'s Mother's Person not included in passoffresult");
            assertNotNull(personFather, relationship + "'s Father's Person not included in passoffresult");
            checkPersonsDeath(eventsResult, personsResult, personFather, relationship + "'s father", generationsLeft - 1);
            checkPersonsDeath(eventsResult, personsResult, personMother, relationship + "'s mother", generationsLeft - 1);
        }
    }

    /**
     * Checks to make sure there are enough people based on the number of generations.
     * (This method is recursive, eventually looking at all the people in the family tree)
     *
     * @param personsResult   List of People
     * @param person          Person we are looking at
     * @param relationship    Relation to User
     * @param generationsLeft Number of generations
     */
    private void checkPersonsParents(PersonsResult personsResult, Person person, String relationship, int generationsLeft) {
        assertNotNull(person, relationship + "'s person not included in passoffresult");
        assertNotNull(person.getFatherID(), relationship + " has no father");
        assertNotNull(person.getMotherID(), relationship + " has no mother");
        assertNotEquals(EMPTY_STRING, person.getFatherID(), relationship + " has no father");
        assertNotEquals(EMPTY_STRING, person.getMotherID(), relationship + " has no mother");

        Person personFather = personsResult.getPerson(person.getFatherID());
        assertNotNull(personFather, relationship + "'s father not included in passoffresult");
        Person personMother = personsResult.getPerson(person.getMotherID());
        assertNotNull(personMother, relationship + "'s mother not included in passoffresult");

        assertNotNull(personFather.getSpouseID(), relationship + "'s father has no spouse");
        assertNotEquals(EMPTY_STRING, personFather.getSpouseID(), relationship + "'s father has no spouse");
        assertNotNull(personMother.getSpouseID(), relationship + "'s mother has no spouse");
        assertNotEquals(EMPTY_STRING, personMother.getSpouseID(), relationship + "'s mother has no spouse");

        assertEquals(personMother.getPersonID(), personFather.getSpouseID(), relationship + "'s father's spouseID does not match " + relationship + "'s mother personID");
        assertEquals(personFather.getPersonID(), personMother.getSpouseID(), relationship + "'s mother's spouseID does not match " + relationship + "'s father personID");

        if (generationsLeft > 0) {
            checkPersonsParents(personsResult, personFather, relationship + "'s father", generationsLeft - 1);
            checkPersonsParents(personsResult, personMother, relationship + "'s mother", generationsLeft - 1);
        } else {
            Assertions.assertTrue(personFather.getFatherID() == null || personFather.getFatherID().equals(EMPTY_STRING), relationship + "'s father has a father. Too many generations");
            Assertions.assertTrue(personFather.getMotherID() == null || personFather.getMotherID().equals(EMPTY_STRING), relationship + "'s father has a mother. Too many generations");
            Assertions.assertTrue(personMother.getFatherID() == null || personMother.getFatherID().equals(EMPTY_STRING), relationship + "'s mother has a father. Too many generations");
            Assertions.assertTrue(personMother.getMotherID() == null || personMother.getMotherID().equals(EMPTY_STRING), relationship + "'s mother has a mother. Too many generations");
        }
    }

    /**
     * Prints the test name
     *
     * @param testInfo The name of the test
     */
    private void printTestName(TestInfo testInfo) {
        if (displayCurrentTest) System.out.println("Running " + testInfo.getDisplayName() + "...");
        logger.info("Running " + testInfo.getDisplayName() + "...");
    }

    /**
     * Reads from a stream created from a file
     *
     * @param is InputSteam to read
     * @return Human readable string
     * @throws IOException Error while running
     */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /**
     * Checks to make sure the latest API call received HTTP_OK headers
     */
    private void assertHTTP_OK() {
        assertEquals(HttpURLConnection.HTTP_OK, Client.getLastResponseCode(), "Response code from server was not HTTP_OK");
    }

    /**
     * Checks to make sure the latest API call received HTTP_BAD_REQUEST
     */
    private void assertHTTP_BAD_REQUEST() {
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, Client.getLastResponseCode(), "Response code from server was not HTTP_BAD_REQUEST");
    }
}
