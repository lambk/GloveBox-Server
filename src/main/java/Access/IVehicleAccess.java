package Access;

import Model.Vehicle;

import java.sql.SQLException;

public interface IVehicleAccess {
    Vehicle getVehicleByPlate(String plate);

    void insertVehicle(Vehicle vehicle, int ownerId) throws SQLException;

    void updateVehicle(Vehicle vehicle) throws SQLException;

    void deleteVehicle(String plate) throws SQLException;
}
