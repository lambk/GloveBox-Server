package Service;

import Model.Vehicle;
import Transfer.VehicleRegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;

public interface IVehicleService {

    void registerVehicle(VehicleRegistrationDTO vehicleRegistrationDTO, String token) throws UnauthorizedException, InternalServerErrorException;

    Vehicle getVehicleInfo(String plate) throws IllegalArgumentException;

}
