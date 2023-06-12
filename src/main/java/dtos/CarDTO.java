package dtos;

import entities.Booking;
import entities.Car;

import java.util.ArrayList;
import java.util.List;

public class CarDTO {
    private String registrationNumber;
    private String brand;
    private String make;
    private Integer year;

    private List<String> bookings = new ArrayList<>();

    public CarDTO(Car car) {
        this.registrationNumber = car.getRegistrationNumber();
        this.brand = car.getBrand();
        this.make = car.getMake();
        this.year = car.getYear();
        for (Booking booking : car.getBookings()) {
            this.bookings.add(booking.toString());
        }
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<String> getBookings() {
        return bookings;
    }

    public void setBookings(List<String> bookings) {
        this.bookings = bookings;
    }


}
