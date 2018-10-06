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
     * Fetches a vehicle record from the database that has the given plate and owner id.
     * Returns a new Vehicle object if a record is found, else returns null
     *
     * @param plate   The plate to find records using
     * @param userID The id of the owner
     * @return The matching vehicle (if found), else null
     */
    @Override
    public Vehicle getVehicle(String plate, int userID) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_vehicle(?,?)}");
            statement.setString(1, plate);
            statement.setInt(2, userID);
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

    /**
     * Fetches a set of vehicles that are linked the user with the given email
     *
     * @param email The email of the account to filter vehicles by
     * @return The set of vehicles
     */
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

    /**
     * Inserts the given vehicle into the Vehicle table, and linking the vehicle to the user account with the given owner id
     *
     * @param vehicle The new vehicle to add
     * @param userID The owner id to use for the owner_id field
     * @throws SQLException If there is an error with the sql operation
     */
    @Override
    public void insertVehicle(Vehicle vehicle, int userID) throws SQLException {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL insert_vehicle(?,?,?,?,?,?,?,?)}");
            statement.setInt(1, userID);
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
    }

    @Override
    public void updateVehicle(Vehicle vehicle) throws SQLException {

    }

    @Override
    public void deleteVehicle(String plate) throws SQLException {

    }
}
