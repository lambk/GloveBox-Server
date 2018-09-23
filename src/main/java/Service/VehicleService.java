package Service;

import Access.IVehicleAccess;
import Mappers.IMapper;
import Mappers.VehicleMapper;
import Model.User;
import Model.Vehicle;
import Transfer.VehicleRegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Set;

@Service
public class VehicleService implements IVehicleService {

    private final IVehicleAccess vehicleAccess;
    private final IAuthService authService;
    private final IMapper<VehicleRegistrationDTO, Vehicle> vehicleMapper;

    @Autowired
    public VehicleService(IVehicleAccess vehicleAccess, IAuthService authService) {
        this.vehicleAccess = vehicleAccess;
        this.authService = authService;
        vehicleMapper = new VehicleMapper();
    }

    @Override
    public void registerVehicle(VehicleRegistrationDTO vehicleRegistrationDTO, String token) throws UnauthorizedException, IllegalArgumentException, InternalServerErrorException {
        User user = authService.getUserByToken(token);
        if (user == null) {
            throw new UnauthorizedException("The given token is not valid");
        }
        if (getVehicleInfo(vehicleRegistrationDTO.getPlate(), user.getId()) != null) {
            throw new IllegalArgumentException("This user already registered a vehicle with the same plate");
        }
        Vehicle vehicle = vehicleMapper.map(vehicleRegistrationDTO);
        try {
            vehicleAccess.insertVehicle(vehicle, user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("The vehicle could not be inserted");
        }
    }

    @Override
    public Vehicle getVehicleInfo(String plate, int ownerId) {
        return vehicleAccess.getVehicle(plate, ownerId);
    }

    @Override
    public Set<Vehicle> getUsersVehicles(String email) {
        Set<Vehicle> vehicles = vehicleAccess.getVehiclesByEmail(email);
        return vehicles;
    }
}
