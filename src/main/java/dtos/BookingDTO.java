package dtos;

import entities.Booking;
import entities.WashingAssistant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDTO {
    private Integer id;
    private LocalDateTime dateAndTime;
    private Integer duration;
    private List<WashingAssistantDTO> washingAssistants = new ArrayList<>();

    private CarDTO car;

    private UserDTO user;

    public BookingDTO(Booking booking) {
        this.id = booking.getId();
        this.dateAndTime = booking.getDateAndTime();
        this.duration = booking.getDuration();
        for (WashingAssistant washingAssistant : booking.getWashingAssistants()) {
            this.washingAssistants.add(new WashingAssistantDTO(washingAssistant));
        }
        this.car = new CarDTO(booking.getCar());
        this.user = new UserDTO(booking.getUser());
    }

    public BookingDTO(LocalDateTime dateAndTime, Integer duration, List<WashingAssistantDTO> washingAssistants, CarDTO car, UserDTO user) {
        this.dateAndTime = dateAndTime;
        this.duration = duration;
        this.washingAssistants = washingAssistants;
        this.car = car;
        this.user = user;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<WashingAssistantDTO> getWashingAssistants() {
        return washingAssistants;
    }

    public void setWashingAssistants(List<WashingAssistantDTO> washingAssistants) {
        this.washingAssistants = washingAssistants;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
