package Service;

import Model.Vehicle;
import Transfer.VehicleRegistrationDTO;
import Utility.Exceptions.UnauthorizedException;

import java.util.Set;

public interface IVehicleService {

    void registerVehicle(VehicleRegistrationDTO vehicleRegistrationDTO, int ownerId, String token) throws UnauthorizedException, IllegalArgumentException;

    Vehicle getVehicleInfo(String plate, int ownerId, String token) throws UnauthorizedException;

    Set<Vehicle> getUsersVehicles(String email);
}
