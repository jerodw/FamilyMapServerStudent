package dao;

import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bestEvent = new Event("Biking_123AB4", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);


        Connection conn = db.getConnection();
        eDao = new EventDAO(conn);eDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.find(bestEvent.getEventID());

        assertNotNull(compareTest);

        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insert(bestEvent);

        assertThrows(DataAccessException.class, () -> eDao.insert(bestEvent));
    }


}


