package Service;

import Model.Vehicle;
import Transfer.VehicleRegistrationDTO;
import Utility.Exceptions.UnauthorizedException;

import java.util.Set;

public interface IVehicleService {

    void registerVehicle(VehicleRegistrationDTO vehicleRegistrationDTO, int userID, String token) throws UnauthorizedException, IllegalArgumentException;

    Vehicle getVehicleInfo(String plate, int userID, String token) throws UnauthorizedException;

    Set<Vehicle> getUsersVehicles(int userID);
}
