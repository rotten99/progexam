package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "washing_assistant")
@NamedQueries({
        @NamedQuery(name = "WashingAssistant.deleteAll", query = "delete from WashingAssistant w")
})
public class WashingAssistant {

    @Id
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "primary_language", nullable = false, length = 25)
    private String primaryLanguage;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @Column(name = "price_pr_hour", nullable = false)
    private Double pricePrHour;

    @ManyToMany(mappedBy = "washingAssistants" , cascade = CascadeType.REMOVE)
    private List<Booking> bookings = new ArrayList<>();

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Double getPricePrHour() {
        return pricePrHour;
    }

    public void setPricePrHour(Double pricePrHour) {
        this.pricePrHour = pricePrHour;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public WashingAssistant() {
    }

    public WashingAssistant(String name, String primaryLanguage, Integer yearsOfExperience, Double pricePrHour, List<Booking> bookings) {
        this.name = name;
        this.primaryLanguage = primaryLanguage;
        this.yearsOfExperience = yearsOfExperience;
        this.pricePrHour = pricePrHour;
        this.bookings = bookings;
    }

    public WashingAssistant(String name, String primaryLanguage, Integer yearsOfExperience, Double pricePrHour) {
        this.name = name;
        this.primaryLanguage = primaryLanguage;
        this.yearsOfExperience = yearsOfExperience;
        this.pricePrHour = pricePrHour;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
    }

    public void clearAll() {
        this.bookings.clear();
    }
}