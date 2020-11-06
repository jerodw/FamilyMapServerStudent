package passoff;

import client.Client;
import client.Proxy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Logger;
import logs.InitLogs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import passoffmodels.Event;
import passoffmodels.Person;
import passoffmodels.User;
import passoffrequest.FillRequest;
import passoffrequest.LoadRequest;
import passoffrequest.LoginRequest;
import passoffrequest.RegisterRequest;
import passoffresult.ClearResult;
import passoffresult.EventResult;
import passoffresult.EventsResult;
import passoffresult.FillResult;
import passoffresult.LoadResult;
import passoffresult.LoginResult;
import passoffresult.PersonResult;
import passoffresult.PersonsResult;
import passoffresult.RegisterResult;

/**
 * This is a mock client used for pass off of the CS240 Family Map Server project.
 * Since the next project is an Android Client, we decided to obfuscate this code
 * in order to preserve the integrity of the learning process for the next project.
 *
 * However, obfuscated code is intentionally hard to read. Therefore, by mercy of
 * the professors, we have included comments for you in order to know exactly what
 * is happening in each test without giving you the actual code.
 *
 * REMINDER: This is the pass off driver. This is not a class of free tests that you
 * can use to debug your own program. Write your own tests first! Verify functionality
 * by running api commands from your own computer using localhost. After your responses
 * look correct on the web api, then you may move on to using this test driver.
 */
public class ServerTest {
    private static final Logger logger;

    private static final User SHEILA;

    private static final User PATRICK;

    private static final LoginRequest loginRequest;

    private static final LoginRequest loginRequest2;

    private static final RegisterRequest registerRequest;

    private static final String BIRTH_EVENT;

    private static final String MARRIAGE_EVENT;

    private static final String DEATH_EVENT;

    private static final String ASTEROIDS1_EVENT_ID;

    private static final String ASTEROIDS2_EVENT_ID;

    private static final String INDEX_HTML_PATH;

    private static final String PAGE_NOT_FOUND_HTML_PATH;

    private static final String MAIN_CSS_PATH;

    private static final int MIN_REALISTIC_MARRIAGE_AGE;

    private static final int MIN_REALISTIC_PREGNANT_AGE;

    private static final int MAX_REALISTIC_PREGNANT_AGE;

    private static final int MAX_REALISTIC_DEATH_AGE;

    private static final String EMPTY_STRING;

    private static final Gson GSON;

    private static String host = "localhost";

    private static String port = "8080";

    private static boolean displayCurrentTest;

    private Proxy proxy;

    public static void setHost(String paramString) { host = paramString; }

    public static void setPort(String paramString) { port = paramString; }

    public static void setDisplayCurrentTest(boolean paramBoolean) { displayCurrentTest = paramBoolean; }

    /**
     * Attempts to make a connection to your server that you wrote
     */
    @BeforeEach
    @DisplayName("Setup")
    public void setup(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        logger.info("Setting up " + paramTestInfo.getDisplayName() + "...");
        this.proxy = new Proxy();
        //If the test/s fail on line 119, make sure the host (line 95) and port (line 97)
        //variables are set to the correct parameters
        this.proxy.b(host, port);
    }

    /**
     * Required API calls:
     * Register
     */
    @Test
    @DisplayName("Register Valid New User Test")
    public void testValidNewRegister(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if registerResult has an authToken String
            Assertions.assertNotNull(registerResult.c(), "authToken was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled registerResult with an authToken String
            Assertions.assertNotEquals(EMPTY_STRING, registerResult.c(), "authToken was empty string, expected non-empty authToken string");
            //Checks to see if registerResult has a personID String
            Assertions.assertNotNull(registerResult.e(), "personID was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled registerResult with a personID String
            Assertions.assertNotEquals(EMPTY_STRING, registerResult.e(), "personID was empty string, expected non-empty string containing the personID of the user's generated Person object");
            //Checks to see if registerResult has a userName String
            Assertions.assertNotNull(registerResult.d(), "userName was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled registerResult with a userName String
            Assertions.assertNotEquals(EMPTY_STRING, registerResult.d(), "userName was empty string, expected userName passed in with passoff request");
            //Checks to see if the userName string matches the one that was in the request (sheila)
            Assertions.assertEquals(SHEILA.g(), registerResult.d(), "userName from Server does not match the requested userName");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     */
    @Test
    @DisplayName("Re-Register User Test")
    public void testReRegister(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //Checks to see if registerResult has an authToken String
            Assertions.assertNotNull(registerResult.c(), "authToken was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled registerResult with an authToken String
            Assertions.assertNotEquals(EMPTY_STRING, registerResult.c(), "authToken was empty string, expected non-empty authToken string");
            //We are calling the register api for a user named sheila
            registerResult = this.proxy.b(host, port, registerRequest);
            //Registering the same userName is invalid, so the headers sent should be HTTP_BAD_REQUEST (400)
            assertHTTP_BAD_REQUEST();
            //registerResult should be invalid, so authToken should be null
            Assertions.assertNull(registerResult.c(), "authToken was not null when it should have been (see API)");
            //registerResult should be invalid, so personID should be null
            Assertions.assertNull(registerResult.e(), "personID was not null when it should have been (see API)");
            //registerResult should be invalid, so the message variable should contain a non-null error string
            Assertions.assertNotNull(registerResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Non-null error strings should include the word "error"
            Assertions.assertTrue(registerResult.b().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Login
     */
    @Test
    @DisplayName("Login Valid User Test")
    public void testValidUserLogin(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the login api for a user named sheila
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if loginResult has an authToken String
            Assertions.assertNotNull(loginResult.c(), "authToken was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loginResult with an authToken String
            Assertions.assertNotEquals(EMPTY_STRING, loginResult.c(), "authToken was empty string, expected non-empty authToken string");
            //Checks to see if loginResult has a personID String
            Assertions.assertNotNull(loginResult.e(), "personID was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loginResult with a personID String
            Assertions.assertNotEquals(EMPTY_STRING, loginResult.e(), "personID was empty string, expected non-empty string containing the personID of the user's generated Person object");
            //Checks to see if loginResult and registerResult have the same generated personID string
            Assertions.assertEquals(registerResult.e(), loginResult.e(), "personID does not match the personID that was returned from register");
            //Checks to see if loginResult has a userName String
            Assertions.assertNotNull(loginResult.d(), "userName was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loginResult with a userName String
            Assertions.assertNotEquals(EMPTY_STRING, loginResult.d(), "userName was empty string, expected userName passed in with passoff request");
            //Checks to see if the userName string from loginResult matches the userName that was in both requests (sheila)
            Assertions.assertEquals(SHEILA.g(), loginResult.d(), "userName from Server does not match the requested userName ");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Login
     */
    @Test
    @DisplayName("Login Invalid User Test")
    public void testInvalidUserLogin(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            this.proxy.b(host, port, registerRequest);
            //We are calling the login api for a user named patrick
            LoginResult loginResult = this.proxy.b(host, port, loginRequest2);
            //We just tried logging in with an invalid user, this checks to make sure loginResult reflects that
            b(loginResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Login
     */
    @Test
    @DisplayName("Login Invalid Password Test")
    public void b(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        //We create a loginRequest with an incorrect password for sheila
        LoginRequest loginRequest1 = new LoginRequest(SHEILA.g(), PATRICK.f());
        if (displayCurrentTest)
            System.out.println("Running Login Invalid Password Test...");
        try {
            //We are calling the register api for a user named sheila
            this.proxy.b(host, port, registerRequest);
            //We are calling the login api for sheila using our bad request we created near the beginning of this test
            LoginResult loginResult = this.proxy.b(host, port, loginRequest1);
            //We just tried logging in with an invalid password, this checks to make sure loginResult reflects that
            b(loginResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testValidPerson(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get single person api using the personID and authToken variable from registerResult on the previous line
            PersonResult personResult = this.proxy.c(host, port, registerResult.c(), registerResult.e());
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to make sure the personResult has a personID that matches the personID requested (using the personID variable from the RegisterResult we got near the beginning of this test)
            Assertions.assertEquals(registerResult.e(), personResult.f(), "personID returned doesn't match personID asked for");
            //Checks to make sure the personResult has a firstName that matches the firstName requested ("Sheila")
            Assertions.assertEquals(registerRequest.d(), personResult.d(), "firstName of person returned does not match that of user's registration");
            //Checks to make sure the personResult has a lastName that matches the lastName requested ("Parker")
            Assertions.assertEquals(registerRequest.c(), personResult.g(), "lastName of person returned does not match that of user's registration");
            //Checks to make sure the personResult has a gender that matches the gender requested ("f")
            Assertions.assertEquals(registerRequest.b(), personResult.b(), "gender of person returned does not match that of user's registration");
            //Checks to make sure the personResult has an associatedUsername that matches the userName requested ("sheila")
            Assertions.assertEquals(registerResult.d(), personResult.e(), "userName of person returned does not match that of user's registration");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testWrongUserPerson(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are calling the login api for a user named sheila
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            //We are calling the get single person api with a personID of "Patrick_Spencer" (we are using the authToken variable from loginResult on the previous line)
            //"Patrick_Spencer" is a person from a different family tree!
            PersonResult personResult = this.proxy.c(host, port, loginResult.c(), PATRICK.d());
            //Trying to access a person that is not related to you is invalid, so the headers sent should be HTTP_BAD_REQUEST (400)
            assertHTTP_BAD_REQUEST();
            //personResult should be invalid, so gender should be null
            Assertions.assertNull(personResult.b(), "gender of invalidly requested person was given");
            //personResult should be invalid, so motherID should be null
            Assertions.assertNull(personResult.j(), "motherID of invalidly requested person was given");
            //personResult should be invalid, so fatherID should be null
            Assertions.assertNull(personResult.c(), "fatherID of invalidly requested person was given");
            //personResult should be invalid, so spouseID should be null
            Assertions.assertNull(personResult.h(), "spouseID of invalidly requested person was given");
            //personResult should be invalid, so userName should be null
            Assertions.assertNull(personResult.e(), "userName of invalidly requested person was given");
            //personResult should be invalid, so firstName should be null
            Assertions.assertNull(personResult.d(), "firstName of invalidly requested person was given");
            //personResult should be invalid, so lastName should be null
            Assertions.assertNull(personResult.g(), "lastName of invalidly requested person was given");
            //personResult should be invalid, so the message variable should contain a non-null error string
            Assertions.assertNotNull(personResult.i(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Non-null error strings should not be left empty
            Assertions.assertNotEquals(EMPTY_STRING, personResult.i(), "message was empty string, should have contained an error message");
            //Non-null error strings should include the word "error"
            Assertions.assertTrue(personResult.i().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * (Partial) Person/[personID]
     */
    @Test
    @DisplayName("Person Bad Auth Token Test")
    public void testBadAuthTokenPerson(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get single person api using the personID variable from registerResult on the previous line
            //However, here we are using an authToken that is not registered with this user
            PersonResult personResult = this.proxy.c(host, port, "bad auth", registerResult.e());
            //Trying to access a person with an unregistered authToken is invalid, so the headers sent should be HTTP_BAD_REQUEST (400)
            assertHTTP_BAD_REQUEST();
            //registerResult should be invalid, so the message variable should contain a non-null error string
            Assertions.assertNotNull(personResult.i(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Non-null error strings should not be left empty
            Assertions.assertNotEquals(EMPTY_STRING, personResult.i(), "message was empty string, should have contained an error message");
            //Non-null error strings should include the word "error"
            Assertions.assertTrue(personResult.i().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testValidPersons(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authToken variable from registerResult on the previous line)
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Check to see that sheila's person information is in the list of people inside the personsResult
            Assertions.assertNotNull(personsResult.c(registerResult.e()), "User's person not found in personsResult result");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * (partial) All Person
     */
    @Test
    @DisplayName("Persons Bad Auth Token Test")
    public void testBadAuthTokenPersons(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            this.proxy.b(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila
            //However, here we are using an authToken that is not registered with this user
            PersonsResult personsResult = this.proxy.d(host, port, "bad auth");
            //We just tried accessing sheila's family tree with an invalid authToken, this checks to make sure personsResult reflects that
            b(personsResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testValidEvent(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            //We are calling the login api for a user named sheila
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            //We are calling the get single event api with an eventID of "Sheila_Asteroids" (we are using the authToken variable from loginResult on the previous line)
            EventResult eventResult = this.proxy.b(host, port, loginResult.c(), ASTEROIDS1_EVENT_ID);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to make sure eventResult has information for an Event [OR] in the case that there isn't, that there is no error message String
            Assertions.assertTrue((eventResult.g() == null || !eventResult.g().toLowerCase().contains("error")), "Result contains an error message");
            //Checks to make sure eventResult has the same information for the "Sheila_Asteroids" Event as it is listed in loadRequest
            Assertions.assertEquals(loadRequest.d(ASTEROIDS1_EVENT_ID), eventResult.b(), "Event returned does not match event from LoadRequest");
            //We are calling the get single event api with an eventID of "Other_Asteroids" (we are using the authToken variable from the same loginResult)
            eventResult = this.proxy.b(host, port, loginResult.c(), ASTEROIDS2_EVENT_ID);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to make sure eventResult has information for an Event [OR] in the case that there isn't, that there is no error message String
            Assertions.assertTrue((eventResult.g() == null || !eventResult.g().toLowerCase().contains("error")), "Result contains an error message");
            //Checks to make sure eventResult has the same information for the "Other_Asteroids" Event as it is listed in loadRequest
            Assertions.assertEquals(loadRequest.d(ASTEROIDS2_EVENT_ID), eventResult.b(), "Event returned does not match event from LoadRequest");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }

    /**
     * IT COULD BE POSSIBLE THAT WE ACTUALLY DON'T CHECK FOR AN INVALID AUTH TOKEN HERE
     * BUT GET AN ERROR ONLY BECAUSE THE EVENT ID IS INVALID! //////////////////////////////////////////////
     * Spoke to Spencer about it, but if the issue is not resolved I can do it as well.
     * ///////////////////////////////////////////////////////////////////////////////////////
     * Please remove this dangling javadoc before merging with the master branch.
     * ////////////////////////////////////////////////////////////////////////////////////
     */
    /**
     * Required API calls:
     * Register
     * (Partial) Event/[eventID]
     */
    @Test
    @DisplayName("Event Bad Auth Token Test")
    public void testBadAuthTokenEvent(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get single event api with an invalid eventID and authToken
            //Here we could get an error for the eventID or the authToken...
            EventResult eventResult = this.proxy.b(host, port, "bad auth", registerResult.e());
            //Trying to access an event with an unregistered authToken or eventID is invalid, so the headers sent should be HTTP_BAD_REQUEST (400)
            assertHTTP_BAD_REQUEST();
            //eventResult should be invalid, so the message variable should contain a non-null error string
            Assertions.assertNotNull(eventResult.g(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Non-null error strings should not be left empty
            Assertions.assertNotEquals(EMPTY_STRING, eventResult.g(), "message was empty string, should have contained an error message");
            //Non-null error strings should include the word "error"
            Assertions.assertTrue(eventResult.g().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testWrongUserEvent(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are calling the login api for a user named patrick
            LoginResult loginResult = this.proxy.b(host, port, loginRequest2);
            //We are calling the get single event api with an eventID of "Sheila_Asteroids" (we are using the authToken variable from loginResult on the previous line)
            //"Patrick_Spencer" is a person from a different family tree!
            EventResult eventResult = this.proxy.b(host, port, loginResult.c(), ASTEROIDS1_EVENT_ID);
            //We just tried accessing an event from sheila's family tree with another user's authToken
            //which is invalid, this checks to make sure eventResult reflects that
            b(eventResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testValidEvents(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get all events api for the user sheila (we are using the authToken variable from registerResult on the previous line)
            EventsResult eventsResult = this.proxy.b(host, port, registerResult.c());
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Check to see that sheila has an event with the eventType "birth" in the list of events inside the eventsResult
            Assertions.assertNotNull(eventsResult.b(registerResult.e(), BIRTH_EVENT), "Result does not contain User's birth");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * (Partial) All Events
     */
    @Test
    @DisplayName("Events Bad Auth Token Test")
    public void testBadAuthTokenEvents(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            this.proxy.b(host, port, registerRequest);
            //We are calling the get all events api for the user sheila
            //However, here we are using an authToken that is not registered with this user
            EventsResult eventsResult = this.proxy.b(host, port, "bad auth");
            //We just tried accessing sheila's family tree of events with an invalid authToken, this checks to make sure personsResult reflects that
            b(eventsResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testValidFillRelationships(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authToken variable from registerResult on the previous line)
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            //Here we are getting the Person information for the user sheila
            Person person = personsResult.c(registerResult.e());
            //Checks to make sure the right amount of people were added to the database after the register service
            b(personsResult, person, "User", 3);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testRealisticBirthEvents(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authToken variable from registerResult on the previous line)
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            //We are calling the get all events api for the user sheila (we are using the authToken variable from the same registerResult)
            EventsResult eventsResult = this.proxy.b(host, port, registerResult.c());
            //Here we are getting the Person information for the user sheila
            Person person = personsResult.c(registerResult.e());
            //If person is null then a Person Object was not created for the user and inserted into the database
            Assertions.assertNotNull(person, "User's Person not included in passoff result");
            //Checks for all the required birth events and makes sure the years make sense
            d(eventsResult, personsResult, person, "User", 3);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testRealisticDeathEvents(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authToken variable from registerResult on the previous line)
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            //We are calling the get all events api for the user sheila (we are using the authToken variable from the same registerResult)
            EventsResult eventsResult = this.proxy.b(host, port, registerResult.c());
            //Here we are getting the Person information for the user sheila
            Person person1 = personsResult.c(registerResult.e());
            //If person1 is null then a Person Object was not created for the user and inserted into the database
            Assertions.assertNotNull(person1, "User's Person not included in passoff result");
            //Here we are getting the Person information for sheila's father
            Person person2 = personsResult.c(person1.c());
            //Here we are getting the Person information for sheila's mother
            Person person3 = personsResult.c(person1.i());
            //If person2 is null then a Person Object was not created for the user's father and inserted into the database
            Assertions.assertNotNull(person2, "User's Father's Person not included in passoff result");
            //If person3 is null then a Person Object was not created for the user's mother and inserted into the database
            Assertions.assertNotNull(person3, "User's Mother's Person not included in passoff result");
            //Checks for all the required death events for the user's mother's side and makes sure the years make sense
            b(eventsResult, personsResult, person3, "User's mother", 3);
            //Checks for all the required death events for the user's father's side and makes sure the years make sense
            b(eventsResult, personsResult, person2, "User's father", 3);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testRealisticFillMarriage(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are calling the register api for a user named sheila
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            //We are calling the get all persons api for the user sheila (we are using the authToken variable from registerResult on the previous line)
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            //We are calling the get all events api for the user sheila (we are using the authToken variable from the same registerResult)
            EventsResult eventsResult = this.proxy.b(host, port, registerResult.c());
            //Here we are getting the Person information for the user sheila
            Person person = personsResult.c(registerResult.e());
            //If person is null then a Person Object was not created for the user and inserted into the database
            Assertions.assertNotNull(person, "User's Person not included in passoff result");
            //Checks for all the required marriage events for all children's parents, ensures the years make sense,
            //and that the marriage is in the same location/year for each couple.
            c(eventsResult, personsResult, person, "User", 2);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testFillDoesNotAffectOtherUsers(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        byte b = 4;
        //We are creating a fill request with the username sheila and the generations as 4
        FillRequest fillRequest = new FillRequest(SHEILA.g(), b);
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            //We are calling the fill api using the fillRequest we created near the beginning of this test
            this.proxy.b(host, port, fillRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //We are calling the login api for a user named patrick
            LoginResult loginResult = this.proxy.b(host, port, loginRequest2);
            //Checks to see if loginResult has an authToken String
            Assertions.assertNotNull(loginResult.c(), "authToken was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loginResult with an authToken String
            Assertions.assertNotEquals(EMPTY_STRING, loginResult.c(), "authToken was empty string, expected non-empty authToken string");
            //We are calling the get all persons api for the user patrick (we are using the authToken variable from the LoginResult we received a few lines up)
            PersonsResult personsResult = this.proxy.d(host, port, loginResult.c());
            //Checks to see if the list of people associated with patrick from loadRequest matches the list of people from personsResult
            Assertions.assertEquals(loadRequest.b(PATRICK.g()), personsResult.d(), "Persons of one user don't match loaded persons after a fill of a different user");
            //We are calling the get all events api for the user patrick (we are using the authToken variable from the same loginResult)
            EventsResult eventsResult = this.proxy.b(host, port, loginResult.c());
            //Checks to see if the list of events associated with patrick from loadRequest matches the list of events from eventsResult
            Assertions.assertEquals(loadRequest.c(PATRICK.g()), eventsResult.d(), "Events of one user don't match loaded events after a fill of a different user");
        } catch (Client.ServerConnectionException | FileNotFoundException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Fill
     */
    @Test
    @DisplayName("Fill 4 Valid Test")
    public void testValidFill4(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        byte b = 4;
        //We are creating a fill request with the username sheila and the generations as 4
        FillRequest fillRequest = new FillRequest(SHEILA.g(), b);
        int i = (int)Math.pow(2.0D, (b + 1)) - 1;
        int j = i * 2;
        try {
            //We are calling the register api for a user named sheila
            this.proxy.b(host, port, registerRequest);
            //We are calling the fill api using the fillRequest we created near the beginning of this test
            FillResult fillResult = this.proxy.b(host, port, fillRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if fillResult has a message String
            Assertions.assertNotNull(fillResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled fillResult with a message String
            Assertions.assertNotEquals(EMPTY_STRING, fillResult.b(), "message was empty string");
            //Splits the fillResult message into four crucial parts
            String[] arrayOfString = fillResult.b().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            //Checks to be sure the fillResult message starts with the phrase "Successfully added "
            Assertions.assertEquals("Successfully added ", arrayOfString[0], "First part of passoff result message does not match API");
            //Checks to be sure the fillResult message confirms that 31 people were added into the database
            Assertions.assertTrue((i <= Integer.parseInt(arrayOfString[1])), "Not enough people added");
            //Checks to be sure the fillResult message has the phrase " persons and " in between listing the number of people and the number of events
            Assertions.assertEquals(" persons and ", arrayOfString[2], "Second part of passoff result message does not match API");
            //Checks to be sure the fillResult message confirms that 91 events were added into the database
            Assertions.assertTrue((j <= Integer.parseInt(arrayOfString[3])), "Not enough events added");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Fill
     */
    @Test
    @DisplayName("Fill 2 Valid Test")
    public void testValidFill2(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        byte b = 2;
        //We are creating a fill request with the username sheila and the generations as 2
        FillRequest fillRequest = new FillRequest(SHEILA.g(), b);
        int i = (int)Math.pow(2.0D, (b + 1)) - 1;
        int j = i * 2;
        try {
            //We are calling the register api for a user named sheila
            this.proxy.b(host, port, registerRequest);
            //We are calling the fill api using the fillRequest we created near the beginning of this test
            FillResult fillResult = this.proxy.b(host, port, fillRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if fillResult has a message String
            Assertions.assertNotNull(fillResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled fillResult with a message String
            Assertions.assertNotEquals(EMPTY_STRING, fillResult.b(), "message was empty string");
            //Splits the fillResult message into four crucial parts
            String[] arrayOfString = fillResult.b().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            //Checks to be sure the fillResult message starts with the phrase "Successfully added "
            Assertions.assertEquals("Successfully added ", arrayOfString[0], "First part of passoff result message does not match API");
            //Checks to be sure the fillResult message confirms that 7 people were added into the database
            Assertions.assertTrue((i <= Integer.parseInt(arrayOfString[1])), "Not enough people added");
            //Checks to be sure the fillResult message has the phrase " persons and " in between listing the number of people and the number of events
            Assertions.assertEquals(" persons and ", arrayOfString[2], "Second part of passoff result message does not match API");
            //Checks to be sure the fillResult message confirms that 19 events were added into the database
            Assertions.assertTrue((j <= Integer.parseInt(arrayOfString[3])), "Not enough events added");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Fill
     */
    @Test
    @DisplayName("Fill 5 Valid Test")
    public void testValidFill5(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        byte b = 5;
        //We are creating a fill request with the username sheila and the generations as 5
        FillRequest fillRequest = new FillRequest(SHEILA.g(), b);
        int i = (int)Math.pow(2.0D, (b + 1)) - 1;
        int j = i * 2;
        try {
            //We are calling the register api for a user named sheila
            this.proxy.b(host, port, registerRequest);
            //We are calling the fill api using the fillRequest we created near the beginning of this test
            FillResult fillResult = this.proxy.b(host, port, fillRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if fillResult has a message String
            Assertions.assertNotNull(fillResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled fillResult with a message String
            Assertions.assertNotEquals(EMPTY_STRING, fillResult.b(), "message was empty string");
            //Splits the fillResult message into four crucial parts
            String[] arrayOfString = fillResult.b().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            //Checks to be sure the fillResult message starts with the phrase "Successfully added "
            Assertions.assertEquals("Successfully added ", arrayOfString[0], "First part of passoff result message does not match API");
            //Checks to be sure the fillResult message confirms that 63 people were added into the database
            Assertions.assertTrue((i <= Integer.parseInt(arrayOfString[1])), "Not enough people added");
            //Checks to be sure the fillResult message has the phrase " persons and " in between listing the number of people and the number of events
            Assertions.assertEquals(" persons and ", arrayOfString[2], "Second part of passoff result message does not match API");
            //Checks to be sure the fillResult message confirms that 187 events were added into the database
            Assertions.assertTrue((j <= Integer.parseInt(arrayOfString[3])), "Not enough events added");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * Register
     * Load
     */
    @Test
    @DisplayName("Load Valid Test")
    public void testValidLoad(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            int i = loadRequest.c().size();
            int j = loadRequest.d().size();
            int k = loadRequest.b().size();
            //We are calling the register api for a user named sheila
            this.proxy.b(host, port, registerRequest);
            //We are calling the load api using the loadRequest we created near the beginning of this test
            LoadResult loadResult = this.proxy.b(host, port, loadRequest);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if loadResult has a message String
            Assertions.assertNotNull(loadResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled loadResult with a message String
            Assertions.assertNotEquals(EMPTY_STRING, loadResult.b(), "message was empty string");
            //Non-null, non-error strings should not include the word "error"
            Assertions.assertFalse(loadResult.b().toLowerCase().contains("error"), "message contained an error");
            //Splits the loadResult message into six crucial parts
            String[] arrayOfString = loadResult.b().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            //Checks to be sure the loadResult message starts with the phrase "Successfully added "
            Assertions.assertEquals("Successfully added ", arrayOfString[0], "First part of passoff result message does not match API");
            //Checks to be sure the loadResult message confirms that 2 users were added into the database
            Assertions.assertEquals(i, Integer.parseInt(arrayOfString[1]), "Incorrect number of users added");
            //Checks to be sure the loadResult message has the phrase " users, " in between listing the number of users and the number of persons
            Assertions.assertEquals(" users, ", arrayOfString[2], "Second part of passoff result message does not match API");
            //Checks to be sure the loadResult message confirms that 11 people were added into the database
            Assertions.assertEquals(j, Integer.parseInt(arrayOfString[3]), "Incorrect number of persons added");
            //Checks to be sure the loadResult message has the phrase " persons, and " in between listing the number of persons and the number of events
            Assertions.assertEquals(" persons, and ", arrayOfString[4], "Third part of passoff result message does not match API");
            //Checks to be sure the loadResult message confirms that 19 events were added into the database
            Assertions.assertEquals(k, Integer.parseInt(arrayOfString[5]), "Incorrect number of events added");
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testValidLoadInfo(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are creating a JsonReader from the LoadData.json file
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            //We are creating a LoadRequest from the JsonReader we made
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);
            //We are calling the login api for a user named sheila
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            //We are calling the get all events api for the user sheila (we are using the authToken variable from loginResult on the previous line)
            EventsResult eventsResult = this.proxy.b(host, port, loginResult.c());
            //We are calling the get all persons api for the user sheila (we are using the authToken variable from the same loginResult)
            PersonsResult personsResult = this.proxy.d(host, port, loginResult.c());
            //Checks to see if the list of events associated with sheila from loadRequest matches the list of events from eventsResult
            Assertions.assertEquals(loadRequest.c(loginRequest.b()), eventsResult.d(), SHEILA.g() + "'s events do not match those loaded");
            //Checks to see if the list of people associated with sheila from loadRequest matches the list of people from personsResult
            Assertions.assertEquals(loadRequest.b(loginRequest.b()), personsResult.d(), SHEILA.g() + "'s persons do not match those loaded");
            //We are calling the login api for a user named patrick
            loginResult = this.proxy.b(host, port, loginRequest2);
            //We are calling the get all events api for the user patrick (we are using the authToken variable from loginResult on the previous line)
            eventsResult = this.proxy.b(host, port, loginResult.c());
            //We are calling the get all persons api for the user patrick (we are using the authToken variable from the same loginResult)
            personsResult = this.proxy.d(host, port, loginResult.c());
            //Checks to see if the list of events associated with patrick from loadRequest matches the list of events from eventsResult
            Assertions.assertEquals(loadRequest.c(loginRequest2.b()), eventsResult.d(), PATRICK.g() + "'s events do not match those loaded");
            //Checks to see if the list of people associated with patrick from loadRequest matches the list of people from personsResult
            Assertions.assertEquals(loadRequest.b(loginRequest2.b()), personsResult.d(), PATRICK.g() + "'s persons do not match those loaded");
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
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
    public void testPersistence(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
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
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            //We are calling the get all events api for the user sheila (we are using the authToken variable from loginResult on the previous line)
            EventsResult eventsResult = this.proxy.b(host, port, loginResult.c());
            //We are calling the get all people api for the user sheila (we are using the authToken variable from the same loginResult)
            PersonsResult personsResult = this.proxy.d(host, port, loginResult.c());
            //Checks to see if the list of events associated with sheila from loadRequest matches the list of events from eventsResult
            Assertions.assertEquals(loadRequest.c(loginRequest.b()), eventsResult.d(), SHEILA.g() + "'s events do not match those loaded");
            //Checks to see if the list of people associated with sheila from loadRequest matches the list of people from personsResult
            Assertions.assertEquals(loadRequest.b(loginRequest.b()), personsResult.d(), SHEILA.g() + "'s persons do not match those loaded");
            //We are calling the login api for a user named patrick
            loginResult = this.proxy.b(host, port, loginRequest2);
            //We are calling the get all events api for the user patrick (we are using the authToken variable from loginResult on the previous line)
            eventsResult = this.proxy.b(host, port, loginResult.c());
            //We are calling the get all people api for the user patrick (we are using the authToken variable from the same loginResult)
            personsResult = this.proxy.d(host, port, loginResult.c());
            //Checks to see if the list of events associated with patrick from loadRequest matches the list of events from eventsResult
            Assertions.assertEquals(loadRequest.c(loginRequest2.b()), eventsResult.d(), PATRICK.g() + "'s events do not match those loaded");
            //Checks to see if the list of people associated with patrick from loadRequest matches the list of people from personsResult
            Assertions.assertEquals(loadRequest.b(loginRequest2.b()), personsResult.d(), PATRICK.g() + "'s persons do not match those loaded");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        } catch (FileNotFoundException fileNotFoundException) {
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
    public void testClear(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        //We are calling the load api using the data in "/passoffFiles/LoadData.json" as the request
        load();
        try {
            //We are calling the login api for a user named sheila
            LoginResult loginResult1 = this.proxy.b(host, port, loginRequest);
            //We are calling the clear api
            ClearResult clearResult = this.proxy.b(host, port);
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if clearResult has a message String
            Assertions.assertNotNull(clearResult.b(), "Clear message was null OR its variable name did not match that of the expected JSon (see API)");
            //Checks to see if you filled clearResult with a message String
            Assertions.assertNotEquals(EMPTY_STRING, clearResult.b(), "Clear message was empty string");
            //Checks to be sure the clearResult message contains the words "clear succeeded"
            Assertions.assertTrue(clearResult.b().toLowerCase().contains("clear succeeded"), "Clear message did not contain the APIs success message");
            //We are calling the login api for a user named sheila
            LoginResult loginResult2 = this.proxy.b(host, port, loginRequest);
            //The database is empty so trying to login at all is invalid, this checks to make sure loginResult2 reflects that
            b(loginResult2);
            //We are calling the login api for a user named patrick
            loginResult2 = this.proxy.b(host, port, loginRequest2);
            //The database is empty so trying to login at all is invalid, this checks to make sure loginResult2 reflects that
            b(loginResult2);
            //We are calling the get all people api for the user sheila (we are using the authToken variable from loginResult1 near the beginning of this test)
            PersonsResult personsResult = this.proxy.d(host, port, loginResult1.c());
            //The database is empty so trying to get a list of people is invalid, this checks to make sure personsResult reflects that
            b(personsResult);
            //We are calling the get all events api for the user sheila (we are using the authToken variable from loginResult1 near the beginning of this test)
            EventsResult eventsResult = this.proxy.b(host, port, loginResult1.c());
            //The database is empty so trying to get a list of events is invalid, this checks to make sure eventsResult reflects that
            b(eventsResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * File Handler
     */
    @DisplayName("File Handler Default Test")
    @Test
    public void testFileHandlerDefault(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        String str = fileToString(INDEX_HTML_PATH);
        assert str != null;
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\n", "");
        try {
            //We are calling the File Handler api with a url of "" (empty string)
            String str1 = this.proxy.c(host, port, EMPTY_STRING);
            str1 = str1.replaceAll("\r", "");
            str1 = str1.replaceAll("\n", "");
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if the File Handler gave a response
            //str1 holds the response the File Handler gave
            Assertions.assertNotNull(str1);
            //Checks to see if the File Handler filled str1 with a String
            Assertions.assertNotEquals(EMPTY_STRING, str1, "Default File Handler returned an empty file");
            //Checks to be sure str1 returned a path to your index.html file
            Assertions.assertEquals(str, str1, "Default File Handler did not return correct file (index.html), or file contents do not exactly match provided file");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * File Handler
     */
    @DisplayName("File Handler Test")
    @Test
    public void testFileHandler(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        String str = fileToString(MAIN_CSS_PATH);
        assert str != null;
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\n", "");
        try {
            //We are calling the File Handler api with a url of "css/main.css"
            String str1 = this.proxy.c(host, port, "css/main.css");
            str1 = str1.replaceAll("\r", "");
            str1 = str1.replaceAll("\n", "");
            //This is a valid api call, so the headers sent should be HTTP_OK (200)
            assertHTTP_OK();
            //Checks to see if the File Handler gave a response
            //str1 holds the response the File Handler gave
            Assertions.assertNotNull(str1);
            //Checks to see if the File Handler filled str1 with a String
            Assertions.assertNotEquals(EMPTY_STRING, str1, "File Handler returned an empty file");
            //Checks to be sure str1 returned a path to your main.css file
            Assertions.assertEquals(str, str1, "File Handler did not return correct file, or file contents do not exactly match provided file");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Required API calls:
     * File Handler
     */
    @DisplayName("File Handler 404 Test")
    @Test
    public void testFileHandler404(TestInfo paramTestInfo) {
        printTestName(paramTestInfo);
        String str = fileToString(PAGE_NOT_FOUND_HTML_PATH);
        assert str != null;
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\n", "");
        try {
            //We are calling the File Handler api with a url of "junkExtension"
            String str1 = this.proxy.c(host, port, "junkExtension");
            str1 = str1.replaceAll("\r", "");
            str1 = str1.replaceAll("\n", "");
            //We gave a path to a file that doesn't exist in your project, so the headers sent should be HTTP_NOT_FOUND (404)
            Assertions.assertEquals(404, Client.b(), "Response code from server was not HTTP_NOT_FOUND");
            //Checks to see if the File Handler gave a response
            //str1 holds the response the File Handler gave
            Assertions.assertNotNull(str1);
            //Checks to see if the File Handler filled str1 with a String
            Assertions.assertNotEquals(EMPTY_STRING, str1, "File Handler returned an empty file");
            //Checks to be sure str1 returned a path to your 404.html file
            Assertions.assertEquals(str, str1, "File Handler did not return correct file, or file contents do not exactly match provided file");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    /**
     * Takes a path to a file and returns its contents. Reports errors
     * if the file cannot be found or if there are errors while reading.
     *
     * @param paramString path to file
     * @return human readable string
     */
    private String fileToString(String paramString) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(paramString));
            return readString(fileInputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            Assertions.fail("Failed to open " + paramString + ". Place it in <project dir>/" + paramString);
        } catch (IOException iOException) {
            iOException.printStackTrace();
            Assertions.fail("Failed to read " + paramString + ". Be sure that you have read access to " + paramString);
        }
        return null;
    }

    /**
     * First checks to make sure the test that called this function received an
     * HTTP_BAD_REQUEST header. Then makes sure all the Event List is
     * empty. Ends by verifying that an error was reported.
     *
     * @param paramEventsResult A EventsResult holding an empty Event List and an error message
     */
    private void b(EventsResult paramEventsResult) {
        assertHTTP_BAD_REQUEST();
        Assertions.assertNull(paramEventsResult.c(), "Events list was given when the auth token was bad");
        Assertions.assertNotNull(paramEventsResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        Assertions.assertNotEquals(EMPTY_STRING, paramEventsResult.b(), "message was empty string, should have contained an error message");
        Assertions.assertTrue(paramEventsResult.b().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    /**
     * First checks to make sure the test that called this function received an
     * HTTP_BAD_REQUEST header. Then makes sure all the Person List is
     * empty. Ends by verifying that an error was reported.
     *
     * @param paramPersonsResult A PersonsResult holding an empty Person List and an error message
     */
    private void b(PersonsResult paramPersonsResult) {
        assertHTTP_BAD_REQUEST();
        Assertions.assertNull(paramPersonsResult.c(), "Persons list was given when the auth token was bad");
        Assertions.assertNotNull(paramPersonsResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        Assertions.assertNotEquals(EMPTY_STRING, paramPersonsResult.b(), "message was empty string, should have contained an error message");
        Assertions.assertTrue(paramPersonsResult.b().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    /**
     * First checks to make sure the test that called this function received an
     * HTTP_BAD_REQUEST header. Then makes sure all the data for a valid
     * loginResult is empty. Ends by verifying that an error was reported.
     *
     * @param paramLoginResult A loginResult holding an error message
     */
    private void b(LoginResult paramLoginResult) {
        assertHTTP_BAD_REQUEST();
        Assertions.assertNull(paramLoginResult.c(), "authToken was not null when it should have been (see API)");
        Assertions.assertNull(paramLoginResult.e(), "personID was not null when it should have been (see API)");
        Assertions.assertNotNull(paramLoginResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        Assertions.assertTrue(paramLoginResult.b().toLowerCase().contains("error"), "message did not contain 'error' string");
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
            this.proxy.b(host, port, loadRequest);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }

    /**
     * First checks to make sure the test that called this function received an
     * HTTP_BAD_REQUEST header. Then makes sure all the data for an Event is
     * empty. Ends by verifying that an error was reported.
     *
     * @param paramEventResult An EventResult holding an empty Event and an error message
     */
    private void b(EventResult paramEventResult) {
        assertHTTP_BAD_REQUEST();
        Assertions.assertNull(paramEventResult.c(), "userName of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.f(), "eventID of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.d(), "personID of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.i(), "eventType of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.e(), "city of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.h(), "country of invalidly requested event was given");
        Assertions.assertNotNull(paramEventResult.g(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        Assertions.assertNotEquals(EMPTY_STRING, paramEventResult.g(), "message was empty string, should have contained an error message");
        Assertions.assertTrue(paramEventResult.g().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    /**
     * Checks to make sure a couples marriage was included in an all events api call,
     * and that the marriage event of a couple are at a realistic age and at the same
     * time/place.
     * (This method is recursive, eventually looking at all the events in the family tree)
     *
     * @param paramEventsResult List of Events
     * @param paramPersonsResult List of People
     * @param paramPerson Person we are looking at
     * @param paramString Relation to the User
     * @param paramInt Number of generations
     */
    private void c(EventsResult paramEventsResult, PersonsResult paramPersonsResult, Person paramPerson, String paramString, int paramInt) {
        Person person1 = paramPersonsResult.c(paramPerson.c());
        Assertions.assertNotNull(person1, paramString + "'s Father's Person not included in passoff result");
        Event event1 = paramEventsResult.b(person1.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event1, paramString + "'s Father's birth Event not included in passoff result");
        int i = event1.f();
        Event event2 = paramEventsResult.b(person1.f(), MARRIAGE_EVENT);
        Assertions.assertNotNull(event2, paramString + "'s Father's marriage Event not included in passoff result");
        int j = event2.f();
        Assertions.assertTrue((j - i >= MIN_REALISTIC_MARRIAGE_AGE), paramString + "'s father was married unrealistically young, min marriage age: " + MIN_REALISTIC_MARRIAGE_AGE);
        Person person2 = paramPersonsResult.c(paramPerson.i());
        Assertions.assertNotNull(person2, paramString + "'s Mother's Person not included in passoff result");
        Event event3 = paramEventsResult.b(person2.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event3, paramString + "'s Mother's birth Event not included in passoff result");
        int k = event3.f();
        Event event4 = paramEventsResult.b(person2.f(), MARRIAGE_EVENT);
        Assertions.assertNotNull(event4, paramString + "'s Mother's marriage Event not included in passoff result");
        int m = event4.f();
        Assertions.assertTrue((m - k >= MIN_REALISTIC_MARRIAGE_AGE), paramString + "'s mother was married unrealistically young, min marriage age: " + MIN_REALISTIC_MARRIAGE_AGE);
        Assertions.assertEquals(event4.f(), event2.f(), paramString + "'s mother and father weren't married on the same day");
        Assertions.assertEquals(event4.g(), event2.g(), paramString + "'s mother and father weren't married in the same city");
        Assertions.assertEquals(event4.i(), event2.i(), paramString + "'s mother and father weren't married in the same country");
        if (paramInt > 0) {
            c(paramEventsResult, paramPersonsResult, person1, paramString + "'s father", paramInt - 1);
            c(paramEventsResult, paramPersonsResult, person2, paramString + "'s mother", paramInt - 1);
        }
    }

    /**
     * Checks to make sure a person's birth was included in an all events api call,
     * and that the birth event of a person's parents are at a realistic age.
     * (This method is recursive, eventually looking at all the events in the family tree)
     *
     * @param paramEventsResult List of Events
     * @param paramPersonsResult List of People
     * @param paramPerson Person we are looking at
     * @param paramString Relation to User
     * @param paramInt Number of generations
     */
    private void d(EventsResult paramEventsResult, PersonsResult paramPersonsResult, Person paramPerson, String paramString, int paramInt) {
        Event event1 = paramEventsResult.b(paramPerson.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event1, paramString + "'s birth Event not included in passoff result");
        int i = event1.f();
        Person person1 = paramPersonsResult.c(paramPerson.c());
        Assertions.assertNotNull(person1, paramString + "'s Father's Person not included in passoff result");
        Event event2 = paramEventsResult.b(person1.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event2, paramString + "'s Father's birth Event not included in passoff result");
        int j = event2.f();
        int k = i - j;
        Assertions.assertTrue((k >= MIN_REALISTIC_PREGNANT_AGE), paramString + "'s father was unrealistically young at user's birth, min age of fatherhood: " + MIN_REALISTIC_PREGNANT_AGE);
        Person person2 = paramPersonsResult.c(paramPerson.i());
        Assertions.assertNotNull(person2, paramString + "'s Mother's Person not included in passoff result");
        Event event3 = paramEventsResult.b(person2.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event3, paramString + "'s Mother's birth Event not included in passoff result");
        int m = event3.f();
        int n = i - m;
        Assertions.assertTrue((n >= MIN_REALISTIC_PREGNANT_AGE), paramString + "'s mother was unrealistically young at user's birth, min pregnant age: " + MIN_REALISTIC_PREGNANT_AGE);
        Assertions.assertTrue((n <= MAX_REALISTIC_PREGNANT_AGE), paramString + "'s mother was unrealistically old at user's birth, max pregnant age: " + MAX_REALISTIC_PREGNANT_AGE);
        if (paramInt > 0) {
            d(paramEventsResult, paramPersonsResult, person1, paramString + "'s father", paramInt - 1);
            d(paramEventsResult, paramPersonsResult, person2, paramString + "'s mother", paramInt - 1);
        }
    }

    /**
     * Checks to make sure a person's death was included in an all events api call,
     * and that the death event is at a realistic age.
     * (This method is recursive, eventually looking at all the events in the family tree)
     *
     * @param paramEventsResult List of Events
     * @param paramPersonsResult List of People
     * @param paramPerson Person we are looking at
     * @param paramString Relation to User
     * @param paramInt Number of generations
     */
    private void b(EventsResult paramEventsResult, PersonsResult paramPersonsResult, Person paramPerson, String paramString, int paramInt) {
        Event event1 = paramEventsResult.b(paramPerson.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event1, paramString + "'s birth Event not included in passoff result");
        int i = event1.f();
        Event event2 = paramEventsResult.b(paramPerson.f(), DEATH_EVENT);
        Assertions.assertNotNull(event2, paramString + "'s death Event not included in passoff result");
        int j = event2.f();
        int k = j - i;
        Assertions.assertTrue((k <= MAX_REALISTIC_DEATH_AGE), paramString + " was unrealistically old at his/her death, max death age: " + MAX_REALISTIC_DEATH_AGE);
        Person person1 = paramPersonsResult.c(paramPerson.i());
        Person person2 = paramPersonsResult.c(paramPerson.c());
        if (paramInt > 0) {
            Assertions.assertNotNull(person1, paramString + "'s Mother's Person not included in passoff result");
            Assertions.assertNotNull(person2, paramString + "'s Father's Person not included in passoff result");
            b(paramEventsResult, paramPersonsResult, person2, paramString + "'s father", paramInt - 1);
            b(paramEventsResult, paramPersonsResult, person1, paramString + "'s mother", paramInt - 1);
        }
    }

    /**
     * Checks to make sure there are enough people based on the number of generations.
     * (This method is recursive, eventually looking at all the people in the family tree)
     *
     * @param paramPersonsResult List of People
     * @param paramPerson Person we are looking at
     * @param paramString Relation to User
     * @param paramInt Number of generations
     */
    private void b(PersonsResult paramPersonsResult, Person paramPerson, String paramString, int paramInt) {
        Assertions.assertNotNull(paramPerson, paramString + "'s person not included in passoff result");
        Assertions.assertNotNull(paramPerson.c(), paramString + " has no father");
        Assertions.assertNotNull(paramPerson.i(), paramString + " has no mother");
        Assertions.assertNotEquals(EMPTY_STRING, paramPerson.c(), paramString + " has no father");
        Assertions.assertNotEquals(EMPTY_STRING, paramPerson.i(), paramString + " has no mother");
        Person person1 = paramPersonsResult.c(paramPerson.c());
        Assertions.assertNotNull(person1, paramString + "'s father not included in passoff result");
        Person person2 = paramPersonsResult.c(paramPerson.i());
        Assertions.assertNotNull(person2, paramString + "'s mother not included in passoff result");
        Assertions.assertNotNull(person1.g(), paramString + "'s father has no spouse");
        Assertions.assertNotEquals(EMPTY_STRING, person1.g(), paramString + "'s father has no spouse");
        Assertions.assertNotNull(person2.g(), paramString + "'s mother has no spouse");
        Assertions.assertNotEquals(EMPTY_STRING, person2.g(), paramString + "'s mother has no spouse");
        Assertions.assertEquals(person2.f(), person1.g(), paramString + "'s father's spouseID does not match " + paramString + "'s mother personID");
        Assertions.assertEquals(person1.f(), person2.g(), paramString + "'s mother's spouseID does not match " + paramString + "'s father personID");
        if (paramInt > 0) {
            b(paramPersonsResult, person1, paramString + "'s father", paramInt - 1);
            b(paramPersonsResult, person2, paramString + "'s mother", paramInt - 1);
        } else {
            Assertions.assertTrue((person1.c() == null || person1.c().equals(EMPTY_STRING)), paramString + "'s father has a father. Too many generations");
            Assertions.assertTrue((person1.i() == null || person1.i().equals(EMPTY_STRING)), paramString + "'s father has a mother. Too many generations");
            Assertions.assertTrue((person2.c() == null || person2.c().equals(EMPTY_STRING)), paramString + "'s mother has a father. Too many generations");
            Assertions.assertTrue((person2.i() == null || person2.i().equals(EMPTY_STRING)), paramString + "'s mother has a mother. Too many generations");
        }
    }

    /**
     * Prints the test name
     *
     * @param paramTestInfo The name of the test
     */
    private void printTestName(TestInfo paramTestInfo) {
        if (displayCurrentTest)
            System.out.println("Running " + paramTestInfo.getDisplayName() + "...");
        logger.info("Running " + paramTestInfo.getDisplayName() + "...");
    }

    /**
     * Reads from a stream created from a file
     *
     * @param paramInputStream InputSteam to read
     * @return Human readable string
     * @throws IOException Error while running
     */
    private String readString(InputStream paramInputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(paramInputStream);
        char[] arrayOfChar = new char[1024];
        int i;
        while ((i = inputStreamReader.read(arrayOfChar)) > 0)
            stringBuilder.append(arrayOfChar, 0, i);
        return stringBuilder.toString();
    }

    /**
     * Checks to make sure the latest API call received HTTP_OK headers
     */
    private void assertHTTP_OK() { Assertions.assertEquals(200, Client.b(), "Response code from server was not HTTP_OK"); }

    /**
     * Checks to make sure the latest API call received HTTP_BAD_REQUEST
     */
    private void assertHTTP_BAD_REQUEST() { Assertions.assertEquals(400, Client.b(), "Response code from server was not HTTP_BAD_REQUEST"); }

    //Sets all our global variables, here you can check to make sure what variables have what values.
    static  {
        InitLogs.init();
        logger = Logger.getLogger(Client.class.getName());
        SHEILA = new User("sheila", "parker", "sheila@parker.com", "Sheila", "Parker", "f", "Sheila_Parker");
        PATRICK = new User("patrick", "spencer", "sheila@spencer.com", "Patrick", "Spencer", "m", "Patrick_Spencer");
        loginRequest = new LoginRequest(SHEILA.g(), SHEILA.f());
        loginRequest2 = new LoginRequest(PATRICK.g(), PATRICK.f());
        registerRequest = new RegisterRequest(SHEILA.g(), SHEILA.f(), SHEILA.h(), SHEILA.c(), SHEILA.e(), SHEILA.b());
        BIRTH_EVENT = "birth";
        MARRIAGE_EVENT = "marriage";
        DEATH_EVENT = "death";
        ASTEROIDS1_EVENT_ID = "Sheila_Asteroids";
        ASTEROIDS2_EVENT_ID = "Other_Asteroids";
        INDEX_HTML_PATH = "web/index.html";
        PAGE_NOT_FOUND_HTML_PATH = "web/HTML/404.html";
        MAIN_CSS_PATH = "web/css/main.css";
        MIN_REALISTIC_MARRIAGE_AGE = 13;
        MIN_REALISTIC_PREGNANT_AGE = 13;
        MAX_REALISTIC_PREGNANT_AGE = 50;
        MAX_REALISTIC_DEATH_AGE = 120;
        EMPTY_STRING = "";
        GSON = (new GsonBuilder()).setPrettyPrinting().create();
        displayCurrentTest = false;
    }
}
