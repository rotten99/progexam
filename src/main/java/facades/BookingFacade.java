package facades;

import dtos.BookingDTO;
import dtos.WashingAssistantDTO;
import entities.Booking;
import entities.Car;
import entities.User;
import entities.WashingAssistant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class BookingFacade {

    private static BookingFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private BookingFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static BookingFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new BookingFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //This method get all bookings from the database
    public List<BookingDTO> getAllBookings(){
        EntityManager em = emf.createEntityManager();
        ArrayList<BookingDTO> bookingDTOS = new ArrayList<>();
        try{
            TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b", Booking.class);
            List<Booking> res = query.getResultList();
            for(Booking b : res){
                bookingDTOS.add(new BookingDTO(b));
            }
        }finally{
            em.close();
        }
        return bookingDTOS;
    }

    //This method creates a booking in the database
    public BookingDTO createNewBooking(BookingDTO b){
        EntityManager em = emf.createEntityManager();
        List<WashingAssistant> assistants = new ArrayList<>();
        Car car = null;
        Booking booking = null;
        for(WashingAssistantDTO wa : b.getWashingAssistants()){
            assistants.add(new WashingAssistant(wa.getName(),wa.getPrimaryLanguage(),wa.getYearsOfExperience(), wa.getPricePrHour()));
        }
        try {
            car = em.find(Car.class, b.getCar().getRegistrationNumber());
        } catch (Exception e) {
            System.out.println("Car not found");
        }

        User user = em.find(User.class, b.getUser().getUserName());


        try{
            em.getTransaction().begin();
            if (car == null){
                car = new Car(b.getCar().getRegistrationNumber(),b.getCar().getBrand(),b.getCar().getMake(),b.getCar().getYear());
                em.persist(car);
            }
            booking= new Booking(b.getDateAndTime(),b.getDuration(),assistants, car, user);
            em.persist(booking);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
        return new BookingDTO(booking);
    }

    //This method deletes a booking from the database
    public BookingDTO deleteBooking(int id){
        EntityManager em = emf.createEntityManager();
        Booking booking = em.find(Booking.class, id);
        try{
            em.getTransaction().begin();
            em.remove(booking);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
        return new BookingDTO(booking);
    }

    //This method updates the assistants for a booking in the database
    public BookingDTO updateBookingAssistants(int id, List<WashingAssistant> assistants){
        EntityManager em = emf.createEntityManager();
        Booking booking = em.find(Booking.class, id);
        try{
            em.getTransaction().begin();
            booking.setWashingAssistants(assistants);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
        return new BookingDTO(booking);
    }

    //This method updates the user and/or car for the booking
    public BookingDTO updateBookingUserAndCar(int id, User user, Car car){
        EntityManager em = emf.createEntityManager();
        Booking booking = em.find(Booking.class, id);
        try{
            em.getTransaction().begin();
            booking.setUser(user);
            booking.setCar(car);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
        return new BookingDTO(booking);
    }

    //This method gets all bookings for a specific user
    public List<BookingDTO> getAllBookingsForUser(String userName){
        EntityManager em = emf.createEntityManager();
        ArrayList<BookingDTO> bookingDTOS = new ArrayList<>();
        try{
            TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.user.userName = :userName", Booking.class);
            query.setParameter("userName", userName);
            List<Booking> res = query.getResultList();
            for(Booking b : res){
                bookingDTOS.add(new BookingDTO(b));
            }
        }finally{
            em.close();
        }
        return bookingDTOS;
    }


}
