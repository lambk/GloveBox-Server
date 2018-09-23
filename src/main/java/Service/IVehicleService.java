package Service;

import Model.Vehicle;
import Transfer.VehicleRegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;

import java.util.Set;

public interface IVehicleService {

    void registerVehicle(VehicleRegistrationDTO vehicleRegistrationDTO, String token) throws UnauthorizedException, IllegalArgumentException, InternalServerErrorException;

    Vehicle getVehicleInfo(String plate, int ownerId);

    Set<Vehicle> getUsersVehicles(String email);
}
