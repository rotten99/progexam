package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.BookingFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Path;

@Path("booking")
public class BookingResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final BookingFacade FACADE =  BookingFacade.getFacade(EMF);
}
