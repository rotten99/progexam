package dtos;

import entities.Booking;
import entities.WashingAssistant;

import java.util.ArrayList;
import java.util.List;

public class WashingAssistantDTO {
    private String name;
    private String primaryLanguage;
    private Integer yearsOfExperience;
    private Double pricePrHour;

    private List<String> bookings = new ArrayList<>();

    public WashingAssistantDTO(WashingAssistant wa) {
        this.name = wa.getName();
        this.primaryLanguage = wa.getPrimaryLanguage();
        this.yearsOfExperience = wa.getYearsOfExperience();
        this.pricePrHour = wa.getPricePrHour();
        if (wa.getBookings().size() > 0) {
            for (Booking booking : wa.getBookings()) {
                this.bookings.add(booking.toString());
            }
        }
    }

    public WashingAssistantDTO(String name, String primaryLanguage, Integer yearsOfExperience, Double pricePrHour) {
        this.name = name;
        this.primaryLanguage = primaryLanguage;
        this.yearsOfExperience = yearsOfExperience;
        this.pricePrHour = pricePrHour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Double getPricePrHour() {
        return pricePrHour;
    }

    public void setPricePrHour(Double pricePrHour) {
        this.pricePrHour = pricePrHour;
    }

    public List<String> getBookings() {
        return bookings;
    }

    public void setBookings(List<String> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "WashingAssistantDTO{" +
                "name='" + name + '\'' +
                ", primaryLanguage='" + primaryLanguage + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", pricePrHour=" + pricePrHour +
                ", bookings=" + bookings +
                '}';
    }
}
