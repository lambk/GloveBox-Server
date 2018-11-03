package Service;

import Model.Vehicle;
import Utility.Exceptions.UnauthorizedException;

import java.util.Set;

public interface IVehicleService {

    Vehicle registerVehicle(Vehicle vehicle, int userID, String token) throws UnauthorizedException, IllegalArgumentException;

    Vehicle getVehicleInfo(String plate, int userID, String token) throws UnauthorizedException;

    Set<Vehicle> getUsersVehicles(int userID, String token) throws UnauthorizedException;
}
