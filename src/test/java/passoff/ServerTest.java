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
import models.Event;
import models.Person;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import request.FillRequest;
import request.LoadRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.EventsResult;
import result.FillResult;
import result.LoadResult;
import result.LoginResult;
import result.PersonResult;
import result.PersonsResult;
import result.RegisterResult;

public class ServerTest {
    private static Logger logger;

    private static final User SHEILA;

    private static final User PATRICK;

    private static final LoginRequest loginRequest;

    private static final LoginRequest loginRequest2;

    private static final RegisterRequest registerRequest;

    private static String BIRTH_EVENT;

    private static String MARRIAGE_EVENT;

    private static String DEATH_EVENT;

    private static String ASTEROIDS1_EVENT_ID;

    private static String ASTEROIDS2_EVENT_ID;

    private static String INDEX_HTML_PATH;

    private static String PAGE_NOT_FOUND_HTML_PATH;

    private static String MAIN_CSS_PATH;

    private static int MIN_REALISTIC_MARRIAGE_AGE;

    private static int MIN_REALISTIC_PREGNANT_AGE;

    private static int MAX_REALISTIC_PREGNANT_AGE;

    private static int MAX_REALISTIC_DEATH_AGE;

    private static String EMPTY_STRING;

    private static final Gson GSON;

    private static String host = "localhost";

    private static String port = "8080";

    private static boolean displayCurrentTest;

    private Proxy proxy;

    public static void setHost(String paramString) { host = paramString; }

    public static void setPort(String paramString) { port = paramString; }

    public static void setDisplayCurrentTest(boolean paramBoolean) { displayCurrentTest = paramBoolean; }

    @BeforeEach
    @DisplayName("Setup")
    public void setup(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        logger.info("Setting up " + paramTestInfo.getDisplayName() + "...");
        this.proxy = new Proxy();
        this.proxy.b(host, port);
    }

    @Test
    @DisplayName("Register Valid New User Test")
    public void testValidNewRegister(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            assertHTTP_OK();
            Assertions.assertNotNull(registerResult.c(), "authToken was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, registerResult.c(), "authToken was empty string, expected non-empty authToken string");
            Assertions.assertNotNull(registerResult.e(), "personID was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, registerResult.e(), "personID was empty string, expected non-empty string containing the personID of the user's generated Person object");
            Assertions.assertNotNull(registerResult.d(), "userName was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, registerResult.d(), "userName was empty string, expected userName passed in with request");
            Assertions.assertEquals(SHEILA.g(), registerResult.d(), "userName from Server does not match the requested userName");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Re-Register User Test")
    public void testReRegister(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            Assertions.assertNotNull(registerResult.c(), "authToken was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, registerResult.c(), "authToken was empty string, expected non-empty authToken string");
            registerResult = this.proxy.b(host, port, registerRequest);
            assertHTTP_BAD_REQUEST();
            Assertions.assertNull(registerResult.c(), "authToken was not null when it should have been (see API)");
            Assertions.assertNull(registerResult.e(), "personID was not null when it should have been (see API)");
            Assertions.assertNotNull(registerResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertTrue(registerResult.b().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Login Valid User Test")
    public void testValidUserLogin(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            assertHTTP_OK();
            Assertions.assertNotNull(loginResult.c(), "authToken was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, loginResult.c(), "authToken was empty string, expected non-empty authToken string");
            Assertions.assertNotNull(loginResult.e(), "personID was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, loginResult.e(), "personID was empty string, expected non-empty string containing the personID of the user's generated Person object");
            Assertions.assertEquals(registerResult.e(), loginResult.e(), "personID does not match the personID that was returned from register");
            Assertions.assertNotNull(loginResult.d(), "userName was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, loginResult.d(), "userName was empty string, expected userName passed in with request");
            Assertions.assertEquals(SHEILA.g(), loginResult.d(), "userName from Server does not match the requested userName ");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Login Invalid User Test")
    public void testInvalidUserLogin(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            this.proxy.b(host, port, registerRequest);
            LoginResult loginResult = this.proxy.b(host, port, loginRequest2);
            assertFailedLogin(loginResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Login Invalid Password Test")
    public void b(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        LoginRequest loginRequest1 = new LoginRequest(SHEILA.g(), PATRICK.f());
        if (displayCurrentTest)
            System.out.println("Running Login Invalid Password Test...");
        try {
            this.proxy.b(host, port, registerRequest);
            LoginResult loginResult = this.proxy.b(host, port, loginRequest1);
            assertFailedLogin(loginResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Person Valid Test")
    public void testValidPerson(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            PersonResult personResult = this.proxy.b(host, port, registerResult.c(), registerResult.e());
            assertHTTP_OK();
            Assertions.assertEquals(registerResult.e(), personResult.f(), "personID returned doesn't match personID asked for");
            Assertions.assertEquals(registerRequest.d(), personResult.d(), "firstName of person returned does not match that of user's registration");
            Assertions.assertEquals(registerRequest.c(), personResult.g(), "lastName of person returned does not match that of user's registration");
            Assertions.assertEquals(registerRequest.b(), personResult.b(), "gender of person returned does not match that of user's registration");
            Assertions.assertEquals(registerResult.d(), personResult.e(), "userName of person returned does not match that of user's registration");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Person Wrong User Test")
    public void testWrongUserPerson(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        load();
        try {
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            PersonResult personResult = this.proxy.b(host, port, loginResult.c(), PATRICK.d());
            assertHTTP_BAD_REQUEST();
            Assertions.assertNull(personResult.b(), "gender of invalidly requested person was given");
            Assertions.assertNull(personResult.j(), "motherID of invalidly requested person was given");
            Assertions.assertNull(personResult.c(), "fatherID of invalidly requested person was given");
            Assertions.assertNull(personResult.h(), "spouseID of invalidly requested person was given");
            Assertions.assertNull(personResult.e(), "userName of invalidly requested person was given");
            Assertions.assertNull(personResult.d(), "firstName of invalidly requested person was given");
            Assertions.assertNull(personResult.g(), "lastName of invalidly requested person was given");
            Assertions.assertNotNull(personResult.i(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, personResult.i(), "message was empty string, should have contained an error message");
            Assertions.assertTrue(personResult.i().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Person Bad Auth Token Test")
    public void testBadAuthTokenPerson(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            PersonResult personResult = this.proxy.b(host, port, "bad auth", registerResult.e());
            assertHTTP_BAD_REQUEST();
            Assertions.assertNotNull(personResult.i(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, personResult.i(), "message was empty string, should have contained an error message");
            Assertions.assertTrue(personResult.i().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Persons Valid Test")
    public void testValidPersons(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            assertHTTP_OK();
            Assertions.assertNotNull(personsResult.c(registerResult.e()), "User's person not found in result");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Persons Bad Auth Token Test")
    public void testBadAuthTokenPersons(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            this.proxy.b(host, port, registerRequest);
            PersonsResult personsResult = this.proxy.d(host, port, "bad auth");
            assertFailedPersons(personsResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Event Valid Test")
    public void testValidEvent(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        load();
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = (LoadRequest)GSON.fromJson(jsonReader, LoadRequest.class);
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            EventResult eventResult = this.proxy.c(host, port, loginResult.c(), ASTEROIDS1_EVENT_ID);
            assertHTTP_OK();
            Assertions.assertTrue((eventResult.f() == null || !eventResult.f().toLowerCase().contains("error")), "Result contains an error message");
            Assertions.assertEquals(loadRequest.d(ASTEROIDS1_EVENT_ID), eventResult.h(), "Event returned does not match event from LoadRequest");
            eventResult = this.proxy.c(host, port, loginResult.c(), ASTEROIDS2_EVENT_ID);
            assertHTTP_OK();
            Assertions.assertTrue((eventResult.f() == null || !eventResult.f().toLowerCase().contains("error")), "Result contains an error message");
            Assertions.assertEquals(loadRequest.d(ASTEROIDS2_EVENT_ID), eventResult.h(), "Event returned does not match event from LoadRequest");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }

    @Test
    @DisplayName("Event Bad Auth Token Test")
    public void testBadAuthTokenEvent(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            EventResult eventResult = this.proxy.c(host, port, "bad auth", registerResult.e());
            assertHTTP_BAD_REQUEST();
            Assertions.assertNotNull(eventResult.f(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, eventResult.f(), "message was empty string, should have contained an error message");
            Assertions.assertTrue(eventResult.f().toLowerCase().contains("error"), "message did not contain 'error' string");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Event Wrong User Test")
    public void testWrongUserEvent(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        load();
        try {
            LoginResult loginResult = this.proxy.b(host, port, loginRequest2);
            EventResult eventResult = this.proxy.c(host, port, loginResult.c(), ASTEROIDS1_EVENT_ID);
            assertFailedEvent(eventResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Events Valid Test")
    public void testValidEvents(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            EventsResult eventsResult = this.proxy.b(host, port, registerResult.c());
            assertHTTP_OK();
            Assertions.assertNotNull(eventsResult.b(registerResult.e(), BIRTH_EVENT), "Result does not contain User's birth");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Events Bad Auth Token Test")
    public void testBadAuthTokenEvents(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            this.proxy.b(host, port, registerRequest);
            EventsResult eventsResult = this.proxy.b(host, port, "bad auth");
            assertFailedEvents(eventsResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Valid Fill Relationships Test")
    public void testValidFillRelationships(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            Person person = personsResult.c(registerResult.e());
            checkPersonsParents(personsResult, person, "User", 3);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Realistic Fill Birth Test")
    public void testRealisticBirthEvents(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            EventsResult eventsResult = this.proxy.b(host, port, registerResult.c());
            Person person = personsResult.c(registerResult.e());
            Assertions.assertNotNull(person, "User's Person not included in result");
            checkPersonsBirth(eventsResult, personsResult, person, "User", 3);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Realistic Fill Death Test")
    public void testRealisticDeathEvents(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            EventsResult eventsResult = this.proxy.b(host, port, registerResult.c());
            Person person1 = personsResult.c(registerResult.e());
            Assertions.assertNotNull(person1, "User's Person not included in result");
            Person person2 = personsResult.c(person1.c());
            Person person3 = personsResult.c(person1.i());
            Assertions.assertNotNull(person2, "User's Father's Person not included in result");
            Assertions.assertNotNull(person3, "User's Mother's Person not included in result");
            checkPersonsDeath(eventsResult, personsResult, person3, "User's mother", 3);
            checkPersonsDeath(eventsResult, personsResult, person2, "User's father", 3);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Realistic Fill Marriage Test")
    public void testRealisticFillMarriage(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            RegisterResult registerResult = this.proxy.b(host, port, registerRequest);
            PersonsResult personsResult = this.proxy.d(host, port, registerResult.c());
            EventsResult eventsResult = this.proxy.b(host, port, registerResult.c());
            Person person = personsResult.c(registerResult.e());
            Assertions.assertNotNull(person, "User's Person not included in result");
            checkParentsMarriage(eventsResult, personsResult, person, "User", 2);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Fill Does Not Affect Other Users Test")
    public void testFillDoesNotAffectOtherUsers(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        load();
        byte b = 4;
        FillRequest fillRequest = new FillRequest(SHEILA.g(), b);
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = (LoadRequest)GSON.fromJson(jsonReader, LoadRequest.class);
            this.proxy.b(host, port, fillRequest);
            assertHTTP_OK();
            LoginResult loginResult = this.proxy.b(host, port, loginRequest2);
            Assertions.assertNotNull(loginResult.c(), "authToken was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, loginResult.c(), "authToken was empty string, expected non-empty authToken string");
            PersonsResult personsResult = this.proxy.d(host, port, loginResult.c());
            Assertions.assertEquals(loadRequest.e(PATRICK.g()), personsResult.c(), "Persons of one user don't match loaded persons after a fill of a different user");
            EventsResult eventsResult = this.proxy.b(host, port, loginResult.c());
            Assertions.assertEquals(loadRequest.b(PATRICK.g()), eventsResult.c(), "Events of one user don't match loaded events after a fill of a different user");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail(fileNotFoundException.getMessage());
        }
    }

    @Test
    @DisplayName("Fill 4 Valid Test")
    public void testValidFill4(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        byte b = 4;
        FillRequest fillRequest = new FillRequest(SHEILA.g(), b);
        int i = (int)Math.pow(2.0D, (b + 1)) - 1;
        int j = i * 2;
        try {
            this.proxy.b(host, port, registerRequest);
            FillResult fillResult = this.proxy.b(host, port, fillRequest);
            assertHTTP_OK();
            Assertions.assertNotNull(fillResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, fillResult.b(), "message was empty string");
            String[] arrayOfString = fillResult.b().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            Assertions.assertEquals("Successfully added ", arrayOfString[0], "First part of result message does not match API");
            Assertions.assertTrue((i <= Integer.parseInt(arrayOfString[1])), "Not enough people added");
            Assertions.assertEquals(" persons and ", arrayOfString[2], "Second part of result message does not match API");
            Assertions.assertTrue((j <= Integer.parseInt(arrayOfString[3])), "Not enough events added");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Fill 2 Valid Test")
    public void testValidFill2(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        byte b = 2;
        FillRequest fillRequest = new FillRequest(SHEILA.g(), b);
        int i = (int)Math.pow(2.0D, (b + 1)) - 1;
        int j = i * 2;
        try {
            this.proxy.b(host, port, registerRequest);
            FillResult fillResult = this.proxy.b(host, port, fillRequest);
            assertHTTP_OK();
            Assertions.assertNotNull(fillResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, fillResult.b(), "message was empty string");
            String[] arrayOfString = fillResult.b().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            Assertions.assertEquals("Successfully added ", arrayOfString[0], "First part of result message does not match API");
            Assertions.assertTrue((i <= Integer.parseInt(arrayOfString[1])), "Not enough people added");
            Assertions.assertEquals(" persons and ", arrayOfString[2], "Second part of result message does not match API");
            Assertions.assertTrue((j <= Integer.parseInt(arrayOfString[3])), "Not enough events added");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Fill 5 Valid Test")
    public void testValidFill5(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        byte b = 5;
        FillRequest fillRequest = new FillRequest(SHEILA.g(), b);
        int i = (int)Math.pow(2.0D, (b + 1)) - 1;
        int j = i * 2;
        try {
            this.proxy.b(host, port, registerRequest);
            FillResult fillResult = this.proxy.b(host, port, fillRequest);
            assertHTTP_OK();
            Assertions.assertNotNull(fillResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, fillResult.b(), "message was empty string");
            String[] arrayOfString = fillResult.b().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            Assertions.assertEquals("Successfully added ", arrayOfString[0], "First part of result message does not match API");
            Assertions.assertTrue((i <= Integer.parseInt(arrayOfString[1])), "Not enough people added");
            Assertions.assertEquals(" persons and ", arrayOfString[2], "Second part of result message does not match API");
            Assertions.assertTrue((j <= Integer.parseInt(arrayOfString[3])), "Not enough events added");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Load Valid Test")
    public void testValidLoad(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = (LoadRequest)GSON.fromJson(jsonReader, LoadRequest.class);
            int i = loadRequest.c().size();
            int j = loadRequest.d().size();
            int k = loadRequest.b().size();
            this.proxy.b(host, port, registerRequest);
            LoadResult loadResult = this.proxy.b(host, port, loadRequest);
            assertHTTP_OK();
            Assertions.assertNotNull(loadResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, loadResult.b(), "message was empty string");
            Assertions.assertFalse(loadResult.b().toLowerCase().contains("error"), "message contained an error");
            String[] arrayOfString = loadResult.b().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            Assertions.assertEquals("Successfully added ", arrayOfString[0], "First part of result message does not match API");
            Assertions.assertEquals(i, Integer.parseInt(arrayOfString[1]), "Incorrect number of users added");
            Assertions.assertEquals(" users, ", arrayOfString[2], "Second part of result message does not match API");
            Assertions.assertEquals(j, Integer.parseInt(arrayOfString[3]), "Incorrect number of persons added");
            Assertions.assertEquals(" persons, and ", arrayOfString[4], "Third part of result message does not match API");
            Assertions.assertEquals(k, Integer.parseInt(arrayOfString[5]), "Incorrect number of events added");
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Load Valid Info Test")
    public void testValidLoadInfo(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        load();
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = (LoadRequest)GSON.fromJson(jsonReader, LoadRequest.class);
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            EventsResult eventsResult = this.proxy.b(host, port, loginResult.c());
            PersonsResult personsResult = this.proxy.d(host, port, loginResult.c());
            Assertions.assertEquals(loadRequest.b(loginRequest.b()), eventsResult.c(), SHEILA.g() + "'s events do not match those loaded");
            Assertions.assertEquals(loadRequest.e(loginRequest.b()), personsResult.c(), SHEILA.g() + "'s persons do not match those loaded");
            loginResult = this.proxy.b(host, port, loginRequest2);
            eventsResult = this.proxy.b(host, port, loginResult.c());
            personsResult = this.proxy.d(host, port, loginResult.c());
            Assertions.assertEquals(loadRequest.b(loginRequest2.b()), eventsResult.c(), PATRICK.g() + "'s events do not match those loaded");
            Assertions.assertEquals(loadRequest.e(loginRequest2.b()), personsResult.c(), PATRICK.g() + "'s persons do not match those loaded");
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @Test
    @DisplayName("Persistence Test")
    public void testPersistence(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        load();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Shut down the server, wait a few seconds, then restart the server. Then press ENTER.");
        scanner.nextLine();
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = (LoadRequest)GSON.fromJson(jsonReader, LoadRequest.class);
            LoginResult loginResult = this.proxy.b(host, port, loginRequest);
            EventsResult eventsResult = this.proxy.b(host, port, loginResult.c());
            PersonsResult personsResult = this.proxy.d(host, port, loginResult.c());
            Assertions.assertEquals(loadRequest.b(loginRequest.b()), eventsResult.c(), SHEILA.g() + "'s events do not match those loaded");
            Assertions.assertEquals(loadRequest.e(loginRequest.b()), personsResult.c(), SHEILA.g() + "'s persons do not match those loaded");
            loginResult = this.proxy.b(host, port, loginRequest2);
            eventsResult = this.proxy.b(host, port, loginResult.c());
            personsResult = this.proxy.d(host, port, loginResult.c());
            Assertions.assertEquals(loadRequest.b(loginRequest2.b()), eventsResult.c(), PATRICK.g() + "'s events do not match those loaded");
            Assertions.assertEquals(loadRequest.e(loginRequest2.b()), personsResult.c(), PATRICK.g() + "'s persons do not match those loaded");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }

    @Test
    @DisplayName("Clear Test")
    public void testClear(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        load();
        try {
            LoginResult loginResult1 = this.proxy.b(host, port, loginRequest);
            ClearResult clearResult = this.proxy.b(host, port);
            assertHTTP_OK();
            Assertions.assertNotNull(clearResult.b(), "Clear message was null OR its variable name did not match that of the expected JSon (see API)");
            Assertions.assertNotEquals(EMPTY_STRING, clearResult.b(), "Clear message was empty string");
            Assertions.assertTrue(clearResult.b().toLowerCase().contains("clear succeeded"), "Clear message did not contain the APIs success message");
            LoginResult loginResult2 = this.proxy.b(host, port, loginRequest);
            assertFailedLogin(loginResult2);
            loginResult2 = this.proxy.b(host, port, loginRequest2);
            assertFailedLogin(loginResult2);
            PersonsResult personsResult = this.proxy.d(host, port, loginResult1.c());
            assertFailedPersons(personsResult);
            EventsResult eventsResult = this.proxy.b(host, port, loginResult1.c());
            assertFailedEvents(eventsResult);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @DisplayName("File Handler Default Test")
    @Test
    public void testFileHandlerDefault(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        String str = fileToString(INDEX_HTML_PATH);
        try {
            String str1 = this.proxy.c(host, port, EMPTY_STRING);
            assertHTTP_OK();
            Assertions.assertNotNull(str1);
            Assertions.assertNotEquals(EMPTY_STRING, str1, "Default File Handler returned an empty file");
            Assertions.assertEquals(str, str1, "Default File Handler did not return correct file (index.html), or file contents do not exactly match provided file");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @DisplayName("File Handler Test")
    @Test
    public void testFileHandler(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        String str = fileToString(MAIN_CSS_PATH);
        try {
            String str1 = this.proxy.c(host, port, "css/main.css");
            assertHTTP_OK();
            Assertions.assertNotNull(str1);
            Assertions.assertNotEquals(EMPTY_STRING, str1, "File Handler returned an empty file");
            Assertions.assertEquals(str, str1, "File Handler did not return correct file, or file contents do not exactly match provided file");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

    @DisplayName("File Handler 404 Test")
    @Test
    public void testFileHandler404(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        printTestName(paramTestInfo);
        String str = fileToString(PAGE_NOT_FOUND_HTML_PATH);
        try {
            String str1 = this.proxy.c(host, port, "junkExtension");
            Assertions.assertEquals(Client.b(), 404, "Response code from server was not HTTP_NOT_FOUND");
            Assertions.assertNotNull(str1);
            Assertions.assertNotEquals(EMPTY_STRING, str1, "File Handler returned an empty file");
            Assertions.assertEquals(str, str1, "File Handler did not return correct file, or file contents do not exactly match provided file");
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        }
    }

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

    private void assertFailedEvents(EventsResult paramEventsResult) {
        assertHTTP_BAD_REQUEST();
        Assertions.assertNull(paramEventsResult.c(), "Events list was given when the auth token was bad");
        Assertions.assertNotNull(paramEventsResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        Assertions.assertNotEquals(EMPTY_STRING, paramEventsResult.b(), "message was empty string, should have contained an error message");
        Assertions.assertTrue(paramEventsResult.b().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    private void assertFailedPersons(PersonsResult paramPersonsResult) {
        assertHTTP_BAD_REQUEST();
        Assertions.assertNull(paramPersonsResult.c(), "Persons list was given when the auth token was bad");
        Assertions.assertNotNull(paramPersonsResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        Assertions.assertNotEquals(EMPTY_STRING, paramPersonsResult.b(), "message was empty string, should have contained an error message");
        Assertions.assertTrue(paramPersonsResult.b().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    private void assertFailedLogin(LoginResult paramLoginResult) {
        assertHTTP_BAD_REQUEST();
        Assertions.assertNull(paramLoginResult.c(), "authToken was not null when it should have been (see API)");
        Assertions.assertNull(paramLoginResult.e(), "personID was not null when it should have been (see API)");
        Assertions.assertNotNull(paramLoginResult.b(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        Assertions.assertTrue(paramLoginResult.b().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    private void load() {
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = (LoadRequest)GSON.fromJson(jsonReader, LoadRequest.class);
            this.proxy.b(host, port, loadRequest);
        } catch (client.Client.ServerConnectionException serverConnectionException) {
            Assertions.fail(serverConnectionException.getMessage());
        } catch (FileNotFoundException fileNotFoundException) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }

    private void assertFailedEvent(EventResult paramEventResult) {
        assertHTTP_BAD_REQUEST();
        Assertions.assertNull(paramEventResult.b(), "userName of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.e(), "eventID of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.c(), "personID of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.i(), "eventType of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.d(), "city of invalidly requested event was given");
        Assertions.assertNull(paramEventResult.g(), "country of invalidly requested event was given");
        Assertions.assertNotNull(paramEventResult.f(), "message was null OR its variable name did not match that of the expected JSon (see API)");
        Assertions.assertNotEquals(EMPTY_STRING, paramEventResult.f(), "message was empty string, should have contained an error message");
        Assertions.assertTrue(paramEventResult.f().toLowerCase().contains("error"), "message did not contain 'error' string");
    }

    private void checkParentsMarriage(EventsResult paramEventsResult, PersonsResult paramPersonsResult, Person paramPerson, String paramString, int paramInt) {
        Person person1 = paramPersonsResult.c(paramPerson.c());
        Assertions.assertNotNull(person1, paramString + "'s Father's Person not included in result");
        Event event1 = paramEventsResult.b(person1.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event1, paramString + "'s Father's birth Event not included in result");
        int i = event1.f();
        Event event2 = paramEventsResult.b(person1.f(), MARRIAGE_EVENT);
        Assertions.assertNotNull(event2, paramString + "'s Father's marriage Event not included in result");
        int j = event2.f();
        Assertions.assertTrue((j - i >= MIN_REALISTIC_MARRIAGE_AGE), paramString + "'s father was married unrealistically young, min marriage age: " + MIN_REALISTIC_MARRIAGE_AGE);
        Person person2 = paramPersonsResult.c(paramPerson.i());
        Assertions.assertNotNull(person2, paramString + "'s Mother's Person not included in result");
        Event event3 = paramEventsResult.b(person2.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event3, paramString + "'s Mother's birth Event not included in result");
        int k = event3.f();
        Event event4 = paramEventsResult.b(person2.f(), MARRIAGE_EVENT);
        Assertions.assertNotNull(event4, paramString + "'s Mother's marriage Event not included in result");
        int m = event4.f();
        Assertions.assertTrue((m - k >= MIN_REALISTIC_MARRIAGE_AGE), paramString + "'s mother was married unrealistically young, min marriage age: " + MIN_REALISTIC_MARRIAGE_AGE);
        Assertions.assertEquals(event4.f(), event2.f(), paramString + "'s mother and father weren't married on the same day");
        Assertions.assertEquals(event4.g(), event2.g(), paramString + "'s mother and father weren't married in the same city");
        Assertions.assertEquals(event4.i(), event2.i(), paramString + "'s mother and father weren't married in the same country");
        if (paramInt > 0) {
            checkParentsMarriage(paramEventsResult, paramPersonsResult, person1, paramString + "'s father", paramInt - 1);
            checkParentsMarriage(paramEventsResult, paramPersonsResult, person2, paramString + "'s mother", paramInt - 1);
        }
    }

    private void checkPersonsBirth(EventsResult paramEventsResult, PersonsResult paramPersonsResult, Person paramPerson, String paramString, int paramInt) {
        Event event1 = paramEventsResult.b(paramPerson.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event1, paramString + "'s birth Event not included in result");
        int i = event1.f();
        Person person1 = paramPersonsResult.c(paramPerson.c());
        Assertions.assertNotNull(person1, paramString + "'s Father's Person not included in result");
        Event event2 = paramEventsResult.b(person1.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event2, paramString + "'s Father's birth Event not included in result");
        int j = event2.f();
        int k = i - j;
        Assertions.assertTrue((k >= MIN_REALISTIC_PREGNANT_AGE), paramString + "'s father was unrealistically young at user's birth, min age of fatherhood: " + MIN_REALISTIC_PREGNANT_AGE);
        Person person2 = paramPersonsResult.c(paramPerson.i());
        Assertions.assertNotNull(person2, paramString + "'s Mother's Person not included in result");
        Event event3 = paramEventsResult.b(person2.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event3, paramString + "'s Mother's birth Event not included in result");
        int m = event3.f();
        int n = i - m;
        Assertions.assertTrue((n >= MIN_REALISTIC_PREGNANT_AGE), paramString + "'s mother was unrealistically young at user's birth, min pregnant age: " + MIN_REALISTIC_PREGNANT_AGE);
        Assertions.assertTrue((n <= MAX_REALISTIC_PREGNANT_AGE), paramString + "'s mother was unrealistically old at user's birth, max prenant age: " + MAX_REALISTIC_PREGNANT_AGE);
        if (paramInt > 0) {
            checkPersonsBirth(paramEventsResult, paramPersonsResult, person1, paramString + "'s father", paramInt - 1);
            checkPersonsBirth(paramEventsResult, paramPersonsResult, person2, paramString + "'s mother", paramInt - 1);
        }
    }

    private void checkPersonsDeath(EventsResult paramEventsResult, PersonsResult paramPersonsResult, Person paramPerson, String paramString, int paramInt) {
        Event event1 = paramEventsResult.b(paramPerson.f(), BIRTH_EVENT);
        Assertions.assertNotNull(event1, paramString + "'s birth Event not included in result");
        int i = event1.f();
        Event event2 = paramEventsResult.b(paramPerson.f(), DEATH_EVENT);
        Assertions.assertNotNull(event2, paramString + "'s death Event not included in result");
        int j = event2.f();
        int k = j - i;
        Assertions.assertTrue((k <= MAX_REALISTIC_DEATH_AGE), paramString + " was unrealistically old at his/her death, max death age: " + MAX_REALISTIC_DEATH_AGE);
        Person person1 = paramPersonsResult.c(paramPerson.i());
        Person person2 = paramPersonsResult.c(paramPerson.c());
        if (paramInt > 0) {
            Assertions.assertNotNull(person1, paramString + "'s Mother's Person not included in result");
            Assertions.assertNotNull(person2, paramString + "'s Father's Person not included in result");
            checkPersonsDeath(paramEventsResult, paramPersonsResult, person2, paramString + "'s father", paramInt - 1);
            checkPersonsDeath(paramEventsResult, paramPersonsResult, person1, paramString + "'s mother", paramInt - 1);
        }
    }

    private void checkPersonsParents(PersonsResult paramPersonsResult, Person paramPerson, String paramString, int paramInt) {
        Assertions.assertNotNull(paramPerson, paramString + "'s person not included in result");
        Assertions.assertNotNull(paramPerson.c(), paramString + " has no father");
        Assertions.assertNotNull(paramPerson.i(), paramString + " has no mother");
        Assertions.assertNotEquals(EMPTY_STRING, paramPerson.c(), paramString + " has no father");
        Assertions.assertNotEquals(EMPTY_STRING, paramPerson.i(), paramString + " has no mother");
        Person person1 = paramPersonsResult.c(paramPerson.c());
        Assertions.assertNotNull(person1, paramString + "'s father not included in result");
        Person person2 = paramPersonsResult.c(paramPerson.i());
        Assertions.assertNotNull(person2, paramString + "'s mother not included in result");
        Assertions.assertNotNull(person1.g(), paramString + "'s father has no spouse");
        Assertions.assertNotEquals(EMPTY_STRING, person1.g(), paramString + "'s father has no spouse");
        Assertions.assertNotNull(person2.g(), paramString + "'s mother has no spouse");
        Assertions.assertNotEquals(EMPTY_STRING, person2.g(), paramString + "'s mother has no spouse");
        Assertions.assertEquals(person2.f(), person1.g(), paramString + "'s father's spouseID does not match " + paramString + "'s mother personID");
        Assertions.assertEquals(person1.f(), person2.g(), paramString + "'s mother's spouseID does not match " + paramString + "'s father personID");
        if (paramInt > 0) {
            checkPersonsParents(paramPersonsResult, person1, paramString + "'s father", paramInt - 1);
            checkPersonsParents(paramPersonsResult, person2, paramString + "'s mother", paramInt - 1);
        } else {
            Assertions.assertTrue((person1.c() == null || person1.c().equals(EMPTY_STRING)), paramString + "'s father has a father. Too many generations");
            Assertions.assertTrue((person1.i() == null || person1.i().equals(EMPTY_STRING)), paramString + "'s father has a mother. Too many generations");
            Assertions.assertTrue((person2.c() == null || person2.c().equals(EMPTY_STRING)), paramString + "'s mother has a father. Too many generations");
            Assertions.assertTrue((person2.i() == null || person2.i().equals(EMPTY_STRING)), paramString + "'s mother has a mother. Too many generations");
        }
    }

    private void printTestName(TestInfo paramTestInfo) throws Client.ServerConnectionException {
        if (displayCurrentTest)
            System.out.println("Running " + paramTestInfo.getDisplayName() + "...");
        logger.info("Running " + paramTestInfo.getDisplayName() + "...");
    }

    private String readString(InputStream paramInputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(paramInputStream);
        char[] arrayOfChar = new char[1024];
        int i;
        while ((i = inputStreamReader.read(arrayOfChar)) > 0)
            stringBuilder.append(arrayOfChar, 0, i);
        return stringBuilder.toString();
    }

    private void assertHTTP_OK() { Assertions.assertEquals(200, Client.b(), "Response code from server was not HTTP_OK"); }

    private void assertHTTP_BAD_REQUEST() { Assertions.assertEquals(400, Client.b(), "Response code from server was not HTTP_BAD_REQUEST"); }

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
        host = "localhost";
        port = "8080";
        displayCurrentTest = false;
    }
}
