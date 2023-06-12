package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BookingDTO;
import dtos.WashingAssistantDTO;
import entities.Car;
import entities.User;
import entities.WashingAssistant;
import facades.BookingFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("booking")
public class BookingResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final BookingFacade FACADE =  BookingFacade.getFacade(EMF);


    //This endpoint is for getting all bookings
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookings() {
        return Response.ok().entity(GSON.toJson(FACADE.getAllBookings())).build();
    }

    //This endpoint is for creating a new booking
    @POST
    @Path("create")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createBooking(String booking) {
        BookingDTO bookingDTO = GSON.fromJson(booking, BookingDTO.class);
        BookingDTO createdBooking = FACADE.createNewBooking(bookingDTO);
        return Response.ok().entity(GSON.toJson(createdBooking)).build();
    }

    //This endpoint is for deleting a booking
    @DELETE
    @Path("delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteBooking(@PathParam("id") int id) {
        BookingDTO deletedBooking = FACADE.deleteBooking(id);
        return Response.ok().entity(GSON.toJson(deletedBooking)).build();
    }

    //This endpoint is for updating washing assistants for a booking
    @PUT
    @Path("update/assistants/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateBooking(@PathParam("id") int id, String booking) {
        System.out.println(booking);
        BookingDTO bookingDTO = GSON.fromJson(booking, BookingDTO.class);
        List<WashingAssistant> assistants = new ArrayList<>();
        for (WashingAssistantDTO dto : bookingDTO.getWashingAssistants()) {
            assistants.add(new WashingAssistant(dto.getName(), dto.getPrimaryLanguage(), dto.getYearsOfExperience(), dto.getPricePrHour()));
        }
        BookingDTO updatedBooking = FACADE.updateBookingAssistants(id, assistants);
        return Response.ok().entity(GSON.toJson(updatedBooking)).build();
    }

    //This endpoint is for updating the user and/or car for a booking
    @PUT
    @Path("update/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateBookingExpanded(@PathParam("id") int id, String booking) {
        EntityManager em = EMF.createEntityManager();
        BookingDTO bookingDTO = GSON.fromJson(booking, BookingDTO.class);
        User user = em.find(User.class, bookingDTO.getUserName());
        Car car = em.find(Car.class, bookingDTO.getCar().getRegistrationNumber());
        BookingDTO updatedBooking = FACADE.updateBookingUserAndCar(id, user, car);
        return Response.ok().entity(GSON.toJson(updatedBooking)).build();
    }

    //This endpoint is for getting all bookings for a specific user
    @GET
    @Path("user/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookingsForUser(@PathParam("username") String username) {
        return Response.ok().entity(GSON.toJson(FACADE.getAllBookingsForUser(username))).build();
    }

    //This endpoint is for the getAllCars method
    @GET
    @Path("cars")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCars() {
        return Response.ok().entity(GSON.toJson(FACADE.getAllCars())).build();
    }
}
