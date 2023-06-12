package entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
@NamedQueries({
        @NamedQuery(name = "Booking.deleteAll", query = "delete from Booking b")
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date_and_time", nullable = false)
    private LocalDateTime dateAndTime;

    @Column(name = "duration", nullable = false)
    private Integer duration;


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "booking_washingAssistants",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "washingAssistants_id"))
    private List<WashingAssistant> washingAssistants = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_user_name")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id")
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<WashingAssistant> getWashingAssistants() {
        return washingAssistants;
    }

    public void setWashingAssistants(List<WashingAssistant> washingAssistants) {
        this.washingAssistants = washingAssistants;
    }


    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Booking() {
    }

    public Booking(LocalDateTime dateAndTime, Integer duration, List<WashingAssistant> washingAssistants, Car car, User user) {
        this.dateAndTime = dateAndTime;
        this.duration = duration;
        this.washingAssistants = washingAssistants;
        this.user = user;
        this.car = car;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "dateAndTime = " + dateAndTime + ", " +
                "duration = " + duration + ", " +
                "user = " + user + ", " +
                "car = " + car + ")";
    }
}