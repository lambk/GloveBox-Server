package Controller;

import Mappers.IMapper;
import Mappers.VehicleMapper;
import Mappers.VehicleRegistrationMapper;
import Model.Vehicle;
import Service.IVehicleService;
import Transfer.VehicleDTO;
import Transfer.VehicleRegistrationDTO;
import Utility.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/vehicles")
public class VehicleController {

    private final IVehicleService vehicleService;
    private final IMapper<VehicleRegistrationDTO, Vehicle> registrationDTOVehicleMapper;
    private final IMapper<Vehicle, VehicleDTO> vehicleVehicleDTOMapper;

    @Autowired
    public VehicleController(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
        this.registrationDTOVehicleMapper = new VehicleRegistrationMapper();
        this.vehicleVehicleDTOMapper = new VehicleMapper();
    }

    /**
     * Calls the service layer, passing the vehicle registration details, and the login token from the request header
     *
     * @param vehicleInfo The vehicle registration details
     * @param userID      The id of the registration owner
     * @param token       The current set token, which ties the registration to a given account
     * @return The ResponseEntity with the outcome of the insert attempt
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "/{userID}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> registerVehicle(@RequestBody VehicleRegistrationDTO vehicleInfo, @PathVariable("userID") int userID, @RequestHeader("token") String token) {
        try {
            Vehicle vehicle = registrationDTOVehicleMapper.map(vehicleInfo);
            Vehicle insertedVehicle = vehicleService.registerVehicle(vehicle, userID, token);
            return new ResponseEntity<>(vehicleVehicleDTOMapper.map(insertedVehicle), HttpStatus.CREATED);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    /**
     * Returns a set of vehicles owned by the user with the given id
     *
     * @param userID The id of the owner
     * @param token  The clients login token
     * @return The ResponseEntity outcome of the fetch attempt
     */
    @CrossOrigin(methods = {RequestMethod.GET})
    @RequestMapping(value = "/{userID}", method = RequestMethod.GET)
    public ResponseEntity<?> getVehicles(@PathVariable("userID") int userID, @RequestHeader("token") String token) {
        try {
            Set<VehicleDTO> vehicleDTOs = new HashSet<>();
            for (Vehicle vehicle : vehicleService.getUsersVehicles(userID, token)) {
                vehicleDTOs.add(vehicleVehicleDTOMapper.map(vehicle));
            }
            return new ResponseEntity<>(vehicleDTOs, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Returns a vehicle object that has the given plate, and belongs to the user with the given id
     * If no vehicle is found, a 404 is returned
     *
     * @param userID The id of the owner
     * @param plate  The plate of the vehicle to fetch
     * @param token  The clients login token
     * @return The ResponseEntity outcome of the fetch attempt
     */
    @CrossOrigin(methods = {RequestMethod.GET})
    @RequestMapping(value = "/{userID}/{plate}", method = RequestMethod.GET)
    public ResponseEntity<?> getVehicle(@PathVariable("userID") int userID, @PathVariable("plate") String plate, @RequestHeader("token") String token) {
        try {
            Vehicle vehicle = vehicleService.getVehicleInfo(plate, userID, token);
            if (vehicle == null) {
                return new ResponseEntity<>("No matching vehicle found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(vehicleVehicleDTOMapper.map(vehicle), HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
