package Service;

import Access.IVehicleAccess;
import Mappers.IMapper;
import Mappers.VehicleMapper;
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
    public VehicleService(IVehicleAccess vehicleAccess, IUserService userService, IAuthService authService) {
        this.vehicleAccess = vehicleAccess;
        this.authService = authService;
        vehicleMapper = new VehicleMapper();
    }

    /**
     * Attempts to register a new vehicle using the vehicle registration details given.
     * Before inserting the new vehicle, the following occurs:
     * - The user with matching token is fetched, and their user id is compared to the id provided
     * - There is a check to make sure a vehicle with the same plate and owner doesn't already exist
     * If the above pass, the access layer is called to add the new vehicle, and it is tied to the user account returned
     * by the token check
     *
     * @param vehicleRegistrationDTO The vehicle registration details
     * @param userID                The users id to register as owner
     * @param token                  The token used to authenticate the provided user id
     * @throws UnauthorizedException        If the token is not valid
     * @throws IllegalArgumentException     If the matched user already has a vehicle with the given plate
     * @throws InternalServerErrorException If there is an error with the sql operation when adding the vehicle
     */
    @Override
    public void registerVehicle(VehicleRegistrationDTO vehicleRegistrationDTO, int userID, String token) throws UnauthorizedException, IllegalArgumentException, InternalServerErrorException {
        if (!authService.isTokenValid(token, userID)) {
            throw new UnauthorizedException("Token does not match given userID");
        }
        if (getVehicleInfo(vehicleRegistrationDTO.getPlate(), userID) != null) {
            throw new IllegalArgumentException("This user already registered a vehicle with the same plate");
        }
        Vehicle vehicle = vehicleMapper.map(vehicleRegistrationDTO);
        try {
            vehicleAccess.insertVehicle(vehicle, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("The vehicle could not be inserted");
        }
    }

    /**
     * Fetches the vehicle info of a vehicle with the given plate and owner id
     *
     * @param plate   The plate of the vehicle
     * @param userID The id of the user account that is linked to the vehicle
     * @param token   The clients login token
     * @return The matched vehicle object (null if there is no match)
     */
    @Override
    public Vehicle getVehicleInfo(String plate, int userID, String token) throws UnauthorizedException {
        if (!authService.isTokenValid(token, userID)) {
            throw new UnauthorizedException("Token does not match given userID");
        }
        return getVehicleInfo(plate, userID);
    }

    /**
     * Fetches the vehicle info of a vehicle with the given plate and owner id
     * Doesn't require authentication (for other service methods that have already authenticated the user
     *
     * @param plate   The plate of the vehicle
     * @param userID The id of the user account that is linked to the vehicle
     * @return The matched vehicle object (null if there is no match)
     */
    private Vehicle getVehicleInfo(String plate, int userID) {
        return vehicleAccess.getVehicle(plate, userID);
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
