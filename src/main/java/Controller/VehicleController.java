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

    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> registerVehicle(@RequestBody VehicleRegistrationDTO vehicleInfo, @RequestHeader("token") String token) {
        try {
            vehicleService.registerVehicle(vehicleInfo, token);
            return new ResponseEntity<>("Vehicle registered successfully", HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @CrossOrigin(methods = {RequestMethod.GET})
    @RequestMapping(value = "/{ownerId}/{plate}", method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity<?> getVehicle(@PathVariable("ownerId") int ownerId, @PathVariable("plate") String plate) {
        Vehicle vehicle = vehicleService.getVehicleInfo(plate, ownerId);
        if (vehicle == null) {
            return new ResponseEntity<>("No matching vehicle found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }
}
