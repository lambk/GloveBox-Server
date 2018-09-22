package Service;

import Model.Vehicle;
import Utility.Exceptions.InternalServerErrorException;

public interface IVehicleService {

    Vehicle getVehicleInfo(String plate) throws IllegalArgumentException, InternalServerErrorException;

}
