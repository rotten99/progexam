package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.WashingAssistantDTO;
import facades.AssistantFacade;
import facades.BookingFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("assistant")
public class AssistantResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final AssistantFacade FACADE =  AssistantFacade.getFacade(EMF);

    //This is the endpoint for getting all assistants
    @GET
    @RolesAllowed("user")
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAssistants() {
        return Response.ok().entity(GSON.toJson(FACADE.getAllWashingAssistants())).build();
    }

    //This is the endpoint for creating a new assistant
    @POST
    @RolesAllowed("admin")
    @Path("create")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createAssistant(String assistant) {
        WashingAssistantDTO assistantDTO = GSON.fromJson(assistant, WashingAssistantDTO.class);
        return Response.ok().entity(GSON.toJson(FACADE.createNewWashingAssistant(assistantDTO))).build();
    }

}
