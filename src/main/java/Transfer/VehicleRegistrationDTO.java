package Transfer;

import java.time.LocalDate;

public class VehicleRegistrationDTO {
    private String plate;
    private String make;
    private String model;
    private int year;
    private int odometer;
    private LocalDate wofExpiry;
    private String countryRegistered;

    public VehicleRegistrationDTO() {}

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
