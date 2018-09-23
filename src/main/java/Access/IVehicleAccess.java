package Access;

import Model.Vehicle;

import java.sql.SQLException;
import java.util.Set;

public interface IVehicleAccess {
    Vehicle getVehicle(String plate, int ownerId);

    Set<Vehicle> getVehiclesByEmail(String email);

    void insertVehicle(Vehicle vehicle, int ownerId) throws SQLException;

    void updateVehicle(Vehicle vehicle) throws SQLException;

    void deleteVehicle(String plate) throws SQLException;
}
