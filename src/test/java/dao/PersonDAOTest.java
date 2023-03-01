package dao;

import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;


public class PersonDAOTest {
    private Database db;
    private Person bestPerson;
    private PersonDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        // and a new event with random data
        bestPerson = new Person("bob12AB", "mrTbone", "bob",
                "bobberton", "m", "juan36GD", "SHIELA566", "dave45GH");

        Connection conn = db.getConnection();
        eDao = new PersonDAO(conn);
        eDao.clear();
    }

    @AfterEach
    public void tearDown() {

        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {

        eDao.insert(bestPerson);
        Person compareTest = eDao.find(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insert(bestPerson);


        assertThrows(DataAccessException.class, () -> eDao.insert(bestPerson));
    }

    @Test
    public void findTest() throws DataAccessException {
        eDao.insert(bestPerson);
        eDao.insert(new Person("ham235", "hammy", "ham", "hammond", "f",null,null, null));
        Person compareTest = eDao.find(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
        Person otherTest = eDao.find("ham235");
        assertNotNull(otherTest);
        assertNotEquals(bestPerson, otherTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        eDao.insert(bestPerson);
        Person nullTest = eDao.find("nonexistent");
        assertNull(nullTest);
    }

    @Test
    public void clearTest() throws DataAccessException {
        eDao.insert(bestPerson);
        eDao.clear();
        assertNull(eDao.find(bestPerson.getPersonID()));
    }
}
