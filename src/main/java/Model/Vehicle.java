package Model;

import java.util.Date;

public class Vehicle {
    private String plate;
    private String make;
    private String model;
    private int year;
    private int odometer;
    private Date wofExpiry;
    private String countryRegistered;

    public Vehicle(String plate, String make, String model, int year, int odometer, Date wofExpiry, String countryRegistered) {
        this.plate = plate;
        this.make = make;
        this.model = model;
        this.year = year;
        this.odometer = odometer;
        this.wofExpiry = wofExpiry;
        this.countryRegistered = countryRegistered;
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

    public Date getWofExpiry() {
        return wofExpiry;
    }

    public String getCountryRegistered() {
        return countryRegistered;
    }
}
