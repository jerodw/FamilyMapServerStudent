package dao;

import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;


public class UserDAOTest {
    private Database db;
    private User bestUser;
    private UserDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        // and a new event with random data
        bestUser = new User("blababab", "mrTbone", "bob@gmail.com",
                "bob", "bobberton", "f", "bob354AB");

        Connection conn = db.getConnection();
        eDao = new UserDAO(conn);
        eDao.clear();
    }

    @AfterEach
    public void tearDown() {

        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {

        eDao.insert(bestUser);
        User compareTest = eDao.find(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insert(bestUser);


        assertThrows(DataAccessException.class, () -> eDao.insert(bestUser));
    }

    @Test
    public void findTest() throws DataAccessException {
        eDao.insert(bestUser);
        eDao.insert(new User("hamm", "hammy", "ham@gmail.com", "ham", "Hammond","m","ham1234"));
        User compareTest = eDao.find(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
        User otherTest = eDao.find("hamm");
        assertNotEquals(bestUser, otherTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        eDao.insert(bestUser);
        User nullTest = eDao.find("nonexistent");
        assertNull(nullTest);
    }

    @Test
    public void clearTest() throws DataAccessException {
        eDao.insert(bestUser);
        eDao.clear();
        assertNull(eDao.find(bestUser.getUsername()));
    }
}
