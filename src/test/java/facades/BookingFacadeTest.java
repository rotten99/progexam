package facades;

import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingFacadeTest {

    private static EntityManagerFactory emf;
    private static BookingFacade facade;

    private Role userRole = new Role("user");
    private List<Role> roles = new ArrayList<>();
    private User user1 = new User("user1", "test123",roles);
    private User user2 = new User("user2", "test123",roles);
    private WashingAssistant wa1 = new WashingAssistant("Hans", "Danish", 2, 100.00);
    private WashingAssistant wa2 = new WashingAssistant("Jens", "Danish", 2, 150.50);
    private Car car1 = new Car("yz31803","toyota","yaris",2010);
    private Car car2 = new Car("xw56804","toyota","yaris2",2017);
    private List<WashingAssistant> waList = new ArrayList<>();

    private Booking booking1 = new Booking(LocalDateTime.now(),2,waList,car1,user1);
    private Booking booking2 = new Booking(LocalDateTime.now(),2,waList,car2,user2);

    public BookingFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = BookingFacade.getFacadeExample(emf);
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
        waList.add(wa1);
        waList.add(wa2);
        roles.add(userRole);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAll").executeUpdate();
            em.createNamedQuery("User.deleteAll").executeUpdate();
            em.createNamedQuery("WashingAssistant.deleteAll").executeUpdate();
            em.createNamedQuery("Booking.deleteAll").executeUpdate();
            em.createNamedQuery("Car.deleteAll").executeUpdate();
            em.persist(userRole);
            em.persist(user1);
            em.persist(user2);
            em.persist(wa1);
            em.persist(wa2);
            em.persist(car1);
            em.persist(car2);
            em.persist(booking1);
            em.persist(booking2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // This test the getBookings method
    @Test
    public void testGetBookings() throws Exception {
        EntityManager em = emf.createEntityManager();

        assertEquals(2, facade.getAllBookings().size(), "Expects two rows in the database");
    }
}
