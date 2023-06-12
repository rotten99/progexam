package rest;

import com.google.gson.Gson;
import dtos.BookingDTO;
import entities.*;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BookingResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static WashingAssistant w1, w2;


    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    Role userRole = new Role("user");
    List<Role> roles = new ArrayList<>();
    User user1 = new User("user1", "test123", roles);
    User user2 = new User("user2", "test123", roles);
    WashingAssistant wa1 = new WashingAssistant("Hans", "Danish", 2, 100.00);
    WashingAssistant wa2 = new WashingAssistant("Jens", "Danish", 2, 150.50);
    Car car1 = new Car("yz31803", "toyota", "yaris", 2010);
    Car car2 = new Car("xw56804", "toyota", "yaris2", 2017);
    List<WashingAssistant> waList = new ArrayList<>();

    Booking booking1 = new Booking(LocalDateTime.now(), 2, waList, car1, user1);
    Booking booking2 = new Booking(LocalDateTime.now(), 2, waList, car2, user2);

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();


        try {
            em.getTransaction().begin();
            em.createNamedQuery("WashingAssistant.deleteAll").executeUpdate();
            em.createNamedQuery("Booking.deleteAll").executeUpdate();
            em.createNamedQuery("Car.deleteAll").executeUpdate();
            em.createNamedQuery("Role.deleteAll").executeUpdate();
            em.createNamedQuery("User.deleteAll").executeUpdate();
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

    //This tests the get all bookings end point in booking resource
    @Test
    public void getAllAssistants() throws Exception {
        given()
                .contentType("application/json")
                .get("/booking/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(2));
    }

    //This tests the create booking end point in booking resource
    @Test
    public void createBooking() throws Exception {
        Booking booking = new Booking(LocalDateTime.now(), 4, waList, car1, user1);
        BookingDTO bookingDTO = new BookingDTO(booking);
        given()
                .contentType("application/json")
                .body(bookingDTO)
                .when()
                .post("/booking/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("car.registrationNumber", equalTo("yz31803"))
                .body("user.userName", equalTo("user1"))
                .body("washingAssistants.size()", equalTo(2));
    }

    //This tests the delete booking end point in booking resource
    @Test
    public void deleteBooking() throws Exception {
        given()
                .contentType("application/json")
                .when()
                .delete("/booking/delete/" + booking1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("car.registrationNumber", equalTo("yz31803"))
                .body("user.userName", equalTo("user1"));
    }

    //This tests the update washing assistant for a booking end point in booking resource
    @Test
    public void updateWashingAssistant() throws Exception {
        List<WashingAssistant> waList = new ArrayList<>();
        ArrayList<WashingAssistant> waList2 = new ArrayList<>();
        waList2.add(wa1);
        Booking booking = new Booking(LocalDateTime.now(), 4, waList2, car1, user1);
        BookingDTO bookingDTO = new BookingDTO(booking);
        String json = new Gson().toJson(bookingDTO);
        given()
                .contentType("application/json")
                .body(json)
                .when()
                .put("/booking/update/assistants/" + booking1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("car.registrationNumber", equalTo("yz31803"))
                .body("user.userName", equalTo("user1"))
                .body("washingAssistants.size()", equalTo(1));
    }

    //This tests the update user and/or car for a booking end point in booking resource
    @Test
    public void updateUserAndCar() throws Exception {
        Booking booking = new Booking(booking1.getDateAndTime(), booking1.getDuration(), waList, car2, user2);
        BookingDTO bookingDTO = new BookingDTO(booking);
        String json = new Gson().toJson(bookingDTO);
        given()
                .contentType("application/json")
                .body(json)
                .when()
                .put("/booking/update/" + booking1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("car.registrationNumber", equalTo(car2.getRegistrationNumber()))
                .body("user.userName", equalTo("user2"))
                .body("washingAssistants.size()", equalTo(2));
    }

    //This tests the get bookings by user end point in booking resource
    @Test
    public void getBookingsByUser() throws Exception {
        given()
                .contentType("application/json")
                .get("/booking/user/" + user1.getUserName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(1))
                .body("[0].car.registrationNumber", equalTo("yz31803"))
                .body("[0].user.userName", equalTo("user1"));
    }

}
