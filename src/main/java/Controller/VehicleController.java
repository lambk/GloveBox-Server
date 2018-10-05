package Controller;

import Model.Vehicle;
import Service.IVehicleService;
import Transfer.VehicleRegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/vehicles", consumes = "application/json")
public class VehicleController {

    private final IVehicleService vehicleService;

    @Autowired
    public VehicleController(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * Calls the service layer, passing the vehicle registration details, and the login token from the request header
     *
     * @param vehicleInfo The vehicle registration details
     * @param ownerId     The id of the registration owner
     * @param token       The current set token, which ties the registration to a given account
     * @return The ResponseEntity with the outcome of the insert attempt
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "/{ownerId}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> registerVehicle(@RequestBody VehicleRegistrationDTO vehicleInfo, @PathVariable("ownerId") int ownerId, @RequestHeader("token") String token) {
        try {
            vehicleService.registerVehicle(vehicleInfo, ownerId, token);
            return new ResponseEntity<>("Vehicle registered successfully", HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    /**
     * Returns a vehicle object that has the given plate, and belongs to the user with the given id
     * If no vehicle is found, a 404 is returned
     *
     * @param ownerId The id of the owner
     * @param plate   The plate of the vehicle to fetch
     * @param token   The clients login token
     * @return The ResponseEntity outcome of the fetch attempt
     */
    @CrossOrigin(methods = {RequestMethod.GET})
    @RequestMapping(value = "/{ownerId}/{plate}", method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity<?> getVehicle(@PathVariable("ownerId") int ownerId, @PathVariable("plate") String plate, @RequestHeader("token") String token) {
        try {
            Vehicle vehicle = vehicleService.getVehicleInfo(plate, ownerId, token);
            if (vehicle == null) {
                return new ResponseEntity<>("No matching vehicle found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }
}
