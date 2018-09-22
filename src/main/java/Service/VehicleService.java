package Service;

import Access.IVehicleAccess;
import Model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService implements IVehicleService {

    private final IVehicleAccess vehicleAccess;

    @Autowired
    public VehicleService(IVehicleAccess vehicleAccess) {
        this.vehicleAccess = vehicleAccess;
    }

    @Override
    public Vehicle getVehicleInfo(String plate) throws IllegalArgumentException {
        Vehicle vehicle = vehicleAccess.getVehicleByPlate(plate);
        if (vehicle == null) {
            throw new IllegalArgumentException("The vehicle with the given plate cannot be found");
        } else {
            return vehicle;
        }
    }
}
