package Transfer;

import java.time.LocalDate;

public class VehicleDTO {
    private Integer id;
    private String plate;
    private String make;
    private String model;
    private int year;
    private int odometer;
    private LocalDate wofExpiry;
    private String countryRegistered;

    public VehicleDTO(Integer id, String plate, String make, String model, int year, int odometer, LocalDate wofExpiry, String countryRegistered) {
        this.id = id;
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
}
