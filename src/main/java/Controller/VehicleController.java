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
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin(methods = {RequestMethod.GET})
    @RequestMapping(value = "/{plate}", method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity<?> getVehicle(@PathVariable("plate") String plate) {
        try {
            Vehicle vehicle = vehicleService.getVehicleInfo(plate);
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
