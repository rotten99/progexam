package facades;

import dtos.UserDTO;
import entities.*;
import security.errorhandling.AuthenticationException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public UserDTO create(UserDTO udto) {
        List<Role> roleList = udto.getRoleList().stream().map(r -> new Role(r.getRoleName())).collect(Collectors.toList());
        User u = new User(udto.getUserName(), udto.getUserPass(), roleList);
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(u);
    }

    //this method deletes a user
    public UserDTO delete(String username) {
        EntityManager em = getEntityManager();
        User u = em.find(User.class, username);
        try {
            em.getTransaction().begin();
            em.remove(u);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(u);
    }

    public List<UserDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> persons = query.getResultList();
        return UserDTO.getDtos(persons);
    }


    public List<UserDTO> getAllUsers() {
        ArrayList<UserDTO> userDTOList = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> users = query.getResultList();
        for (User user : users) {
            userDTOList.add(new UserDTO(user));
        }
        return UserDTO.getDtos(users);
    }

    public void truncateTables() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            em.createNamedQuery("Booking.deleteAll").executeUpdate();
            em.createNamedQuery("Car.deleteAll").executeUpdate();
            em.createNamedQuery("Role.deleteAll").executeUpdate();
            em.createNamedQuery("User.deleteAll").executeUpdate();
            em.createNamedQuery("WashingAssistant.deleteAll").executeUpdate();
        } catch (NullPointerException e) {
            System.out.println("Error in truncateTables: " + e.getMessage());
        }

        em.getTransaction().commit();
        em.close();
    }
}







