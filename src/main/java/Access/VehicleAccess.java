package Access;

import Model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class VehicleAccess implements IVehicleAccess {

    private final ConnectionFactory factory;

    @Autowired
    public VehicleAccess(ConnectionFactory factory) {
        this.factory = factory;
    }

    /**
     * Fetches a vehicle record from the database that has the given plate.
     * Returns a new Vehicle object if a record is found, else returns null
     *
     * @param plate The plate to find records using
     * @return A matching Vehicle record if found, else null
     */
    @Override
    public Vehicle getVehicleByPlate(String plate) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_vehicle_by_plate(?)}");
            statement.setString(1, plate);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Vehicle(resultSet.getString("plate"),
                        resultSet.getString("make"),
                        resultSet.getString("model"),
                        resultSet.getInt("year"),
                        resultSet.getInt("odometer"),
                        resultSet.getDate("wof_expiry").toLocalDate(),
                        resultSet.getString("country_registered"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void insertVehicle(Vehicle vehicle, int ownerId) throws SQLException {
        Connection connection = factory.getConnection();
        PreparedStatement statement = connection.prepareStatement("{CALL insert_vehicle(?,?,?,?,?,?,?,?)}");
        statement.setInt(1, ownerId);
        statement.setString(2, vehicle.getPlate());
        statement.setString(3, vehicle.getMake());
        statement.setString(4, vehicle.getModel());
        statement.setInt(5, vehicle.getYear());
        statement.setInt(6, vehicle.getOdometer());
        statement.setDate(7, Date.valueOf(vehicle.getWofExpiry()));
        statement.setString(8, vehicle.getCountryRegistered());
        statement.executeUpdate();
    }

    @Override
    public void updateVehicle(Vehicle vehicle) throws SQLException {

    }

    @Override
    public void deleteVehicle(String plate) throws SQLException {

    }
}
