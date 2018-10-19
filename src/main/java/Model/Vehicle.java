package Model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String plate;
    private String make;
    private String model;
    private int year;
    private int odometer;
    @Column(name = "wof_expiry")
    private LocalDate wofExpiry;
    @Column(name = "country_registered")
    private String countryRegistered;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    public Vehicle() {
    }

    public Vehicle(String plate, String make, String model, int year, int odometer, LocalDate wofExpiry, String countryRegistered) {
        this.plate = plate;
        this.make = make;
        this.model = model;
        this.year = year;
        this.odometer = odometer;
        this.wofExpiry = wofExpiry;
        this.countryRegistered = countryRegistered;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getOdometer() {
        return odometer;
    }

    public LocalDate getWofExpiry() {
        return wofExpiry;
    }

    public String getCountryRegistered() {
        return countryRegistered;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
