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

    /**
     * Attempts to register a new vehicle using the vehicle registration details given.
     * Before inserting the new vehicle, the following occurs:
     * - The user with matching token is fetched to make sure there is an existing login for that token
     * - There is a check to make sure a vehicle with the same plate and owner doesn't already exist
     * If the above pass, the access layer is called to add the new vehicle, and it is tied to the user account returned
     * by the token check
     *
     * @param vehicleRegistrationDTO The vehicle registration details
     * @param token                  The token used to tie the registration to a logged in user
     * @throws UnauthorizedException        If the token is not valid
     * @throws IllegalArgumentException     If the matched user already has a vehicle with the given plate
     * @throws InternalServerErrorException If there is an error with the sql operation when adding the vehicle
     */
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

    /**
     * Fetches the vehicle info of a vehicle with the given plate and owner id
     *
     * @param plate   The plate of the vehicle
     * @param ownerId The id of the user account that is linked to the vehicle
     * @return The matched vehicle object (null if there is no match)
     */
    @Override
    public Vehicle getVehicleInfo(String plate, int ownerId) {
        return vehicleAccess.getVehicle(plate, ownerId);
    }

    /**
     * Fetches a set of vehicles that are linked the user with the given email
     *
     * @param email The email of the account to filter vehicles by
     * @return The set of vehicles
     */
    @Override
    public Set<Vehicle> getUsersVehicles(String email) {
        return vehicleAccess.getVehiclesByEmail(email);
    }
}
