package Access;

import Model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

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
     * @param ownerId The id of the owner
     * @return The matching vehicle (if found), else null
     */
    @Override
    public Vehicle getVehicle(String plate, int ownerId) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_vehicle(?,?)}");
            statement.setString(1, plate);
            statement.setInt(2, ownerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Vehicle(resultSet.getString("plate").toUpperCase(),
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
    public Set<Vehicle> getVehiclesByEmail(String email) {
        Set<Vehicle> vehicles = new HashSet<>();
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_vehicles_by_email(?)}");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                //todo implement
                return vehicles;
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
        statement.setString(2, vehicle.getPlate().toUpperCase());
        statement.setString(3, vehicle.getMake());
        statement.setString(4, vehicle.getModel());
        statement.setInt(5, vehicle.getYear());
        statement.setInt(6, vehicle.getOdometer());
        if (vehicle.getWofExpiry() != null) {
            statement.setDate(7, Date.valueOf(vehicle.getWofExpiry()));
        } else {
            statement.setDate(7, null);
        }
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
