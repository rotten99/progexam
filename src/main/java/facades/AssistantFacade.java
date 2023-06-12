package facades;

import dtos.BookingDTO;
import dtos.WashingAssistantDTO;
import entities.Booking;
import entities.WashingAssistant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class AssistantFacade {

    private static AssistantFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AssistantFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static AssistantFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AssistantFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //This method gets all WashingAssistants from the database
    public List<WashingAssistantDTO> getAllWashingAssistants(){
        EntityManager em = emf.createEntityManager();
        ArrayList<WashingAssistantDTO> washingAssistantDTOS = new ArrayList<>();
        try{
            TypedQuery<WashingAssistant> query = em.createQuery("SELECT wa FROM WashingAssistant wa", WashingAssistant.class);
            List<WashingAssistant> res = query.getResultList();
            for(WashingAssistant wa : res){
                washingAssistantDTOS.add(new WashingAssistantDTO(wa));
            }
        }finally{
            em.close();
        }
        return washingAssistantDTOS;
    }

    //This method creates a WashingAssistant in the database
    public WashingAssistantDTO createNewWashingAssistant(WashingAssistantDTO wa){
        EntityManager em = emf.createEntityManager();

        WashingAssistant washingAssistant = new WashingAssistant(wa.getName(),wa.getPrimaryLanguage(),wa.getYearsOfExperience(), wa.getPricePrHour());
        try{
            em.getTransaction().begin();
            em.persist(washingAssistant);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
        return new WashingAssistantDTO(washingAssistant);
    }


}
