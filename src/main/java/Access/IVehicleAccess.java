package Access;

import Model.Vehicle;

import java.sql.SQLException;

public interface IVehicleAccess {
    Vehicle getVehicleByPlate();

    void insertVehicle(Vehicle vehicle) throws SQLException;

    void updateVehicle(Vehicle vehicle) throws SQLException;

    void deleteVehicle(String plate) throws SQLException;
}
