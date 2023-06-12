package facades;

import dtos.BookingDTO;
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
    private User user1 = new User("user1", "test123", roles);
    private User user2 = new User("user2", "test123", roles);
    private WashingAssistant wa1 = new WashingAssistant("Hans", "Danish", 2, 100.00);
    private WashingAssistant wa2 = new WashingAssistant("Jens", "Danish", 2, 150.50);
    private Car car1 = new Car("yz31803", "toyota", "yaris", 2010);
    private Car car2 = new Car("xw56804", "toyota", "yaris2", 2017);
    private List<WashingAssistant> waList = new ArrayList<>();

    private Booking booking1 = new Booking("I dag", 2, waList, car1, user1);
    private Booking booking2 = new Booking("i morgen", 2, waList, car2, user2);

    public BookingFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = BookingFacade.getFacade(emf);
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
            UserFacade userFacade = UserFacade.getUserFacade(emf);
            userFacade.truncateTables();
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(user1);
            em.persist(user2);
            em.persist(wa1);
            em.persist(wa2);
            em.persist(car1);
            em.persist(car2);
            waList.add(wa1);
            waList.add(wa2);
            roles.add(userRole);
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


        assertEquals(2, facade.getAllBookings().size(), "Expects two rows in the database");
    }

    //This tests the createBooking method
    @Test
    public void testCreateBooking() throws Exception {
        EntityManager em = emf.createEntityManager();
        Booking booking3 = new Booking("nu", 2, waList, new Car("xu56374", "toyota", "yaris", 1999), user1);
        facade.createNewBooking(new BookingDTO(booking3));
        assertEquals(3, facade.getAllBookings().size(), "Expects three rows in the database");
    }

    //This tests the deleteBooking method
    @Test
    public void testDeleteBooking() throws Exception {
        System.out.println(facade.getAllBookings().size() + "*****************************");
        facade.deleteBooking(booking1.getId());
        assertEquals(1, facade.getAllBookings().size(), "Expects one row in the database");
    }

    //This tests the updateBookingAssistant method
    @Test
    public void testUpdateBookingAssistant() throws Exception {
        waList.remove(wa1);
        facade.updateBookingAssistants(booking1.getId(), waList);
        EntityManager em = emf.createEntityManager();
        Booking booking = em.find(Booking.class, booking1.getId());
        assertEquals(1, booking.getWashingAssistants().size(), "Expects one row in the database");
    }

    //This tests the updateBookingUserAndCar method
    @Test
    public void testUpdateBookingUserAndCar() throws Exception {
        facade.updateBookingUserAndCar(booking1.getId(), user2, car2);
        EntityManager em = emf.createEntityManager();
        Booking booking = em.find(Booking.class, booking1.getId());
        assertEquals("user2", booking.getUser().getUserName(), "Expects user2 to be the user");
        assertEquals("xw56804", booking.getCar().getRegistrationNumber(), "Expects xw56804 to be the license plate");
    }

    //This tests the getBookingsByUser method
    @Test
    public void testGetBookingsByUser() throws Exception {
        assertEquals(1, facade.getAllBookingsForUser(user1.getUserName()).size(), "Expects one row in the database");
    }

    //This tests the getAllCars method
    @Test
    public void testGetAllCars() throws Exception {
        assertEquals(2, facade.getAllCars().size(), "Expects two rows in the database");
    }
}
