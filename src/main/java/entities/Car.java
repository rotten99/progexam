package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car")
@NamedQueries({
        @NamedQuery(name = "Car.deleteAll", query = "delete from Car c")
})
public class Car {


    @Id
    @Column(name = "registration_number", nullable = false, length = 15)
    private String registrationNumber;

    @Column(name = "brand", nullable = false, length = 25)
    private String brand;

    @Column(name = "make", nullable = false, length = 50)
    private String make;

    @Column(name = "year", nullable = false)
    private Integer year;

    @OneToMany(mappedBy = "car", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Booking> bookings = new ArrayList<>();

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Car() {
    }

    public Car(String registrationNumber, String brand, String make, Integer year) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.make = make;
        this.year = year;
    }



}