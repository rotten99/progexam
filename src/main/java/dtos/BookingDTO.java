package dtos;

import entities.Booking;
import entities.WashingAssistant;

import java.util.ArrayList;
import java.util.List;

public class BookingDTO {
    private Integer id;
    private String dateAndTime;
    private Integer duration;
    private List<WashingAssistantDTO> washingAssistants = new ArrayList<>();

    private CarDTO car;

    private String userName;

    public BookingDTO(Booking booking) {
        this.id = booking.getId();
        this.dateAndTime = booking.getDateAndTime();
        this.duration = booking.getDuration();
        for (WashingAssistant washingAssistant : booking.getWashingAssistants()) {
            this.washingAssistants.add(new WashingAssistantDTO(washingAssistant));
        }
        this.car = new CarDTO(booking.getCar());
        this.userName = booking.getUser().getUserName();
    }

    public BookingDTO(String dateAndTime, Integer duration, List<WashingAssistantDTO> washingAssistants, CarDTO car,String userName) {
        this.dateAndTime = dateAndTime;
        this.duration = duration;
        this.washingAssistants = washingAssistants;
        this.car = car;
        this.userName = userName;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
