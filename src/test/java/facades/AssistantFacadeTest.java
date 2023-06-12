package facades;

import dtos.WashingAssistantDTO;
import entities.RenameMe;
import entities.WashingAssistant;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssistantFacadeTest {
    private static EntityManagerFactory emf;
    private static AssistantFacade facade;

    public AssistantFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AssistantFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("WashingAssistant.deleteAll").executeUpdate();
            em.persist(new WashingAssistant("Hans", "Danish", 2, 100.00));
            em.persist(new WashingAssistant("Jens", "Danish", 2, 150.50));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // This test the getAssistants method
    @Test
    public void testGetAssistants() throws Exception {
        assertEquals(2, facade.getAllWashingAssistants().size(), "Expects two rows in the database");
    }

    //This tests the createAssistant method
    @Test
    public void testCreateAssistant() throws Exception {
        facade.createNewWashingAssistant(new WashingAssistantDTO("Kurt", "Danish", 2, 100.00));
        assertEquals(3, facade.getAllWashingAssistants().size(), "Expects three rows in the database");
    }
}
