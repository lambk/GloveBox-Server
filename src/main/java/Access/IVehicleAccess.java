package Access;

import Model.Vehicle;

import java.util.Set;

public interface IVehicleAccess {
    Vehicle getVehicle(String plate, int ownerId);

    Set<Vehicle> getVehiclesByID(int userID);

    void insertVehicle(Vehicle vehicle, int userID);

    void updateVehicle(Vehicle vehicle);

    void deleteVehicle(String plate);
}
