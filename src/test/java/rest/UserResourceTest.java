package rest;

import com.google.gson.Gson;
import dtos.UserDTO;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserResourceTest {


    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static User u1, u2;
    List<Role> userList = new ArrayList<>();


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
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        List<Role> adminList = new ArrayList<>();
        Role uRole = new Role("user");
        Role aRole = new Role("admin");
        userList.add(uRole);
        adminList.add(aRole);
        u1 = new User("user", "123",userList);
        u2 = new User("admin", "123",adminList);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.persist(uRole);
            em.persist(aRole);
            em.persist(u1);
            em.persist(u2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This test, tests the get all users method in the UserResource class
    @Test
    public void getAllUsers() throws Exception {
        given()
                .contentType("application/json")
                .get("/info/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body( equalTo("[2]"));

    }

    //This method test tests the create user method in the UserResource class
    @Test
    public void testCreateUserEndpoint() {

        List<Role> userList = new ArrayList<>();
        User u = new User("johndoe", "password",userList);
        System.out.println("*****************"+u+"*****************"+u.getRoleList());
        UserDTO userDTO = new UserDTO(u);

        String requestBody = new Gson().toJson(userDTO);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/info/create")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("userName", equalTo("johndoe"));
    }

    //This tests the delete user method in the UserResource class
    @Test
    public void testDeleteUserEndpoint() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/info/delete/" + u1.getUserName())
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("userName", equalTo("user"));
    }

}
