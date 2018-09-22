package Controller;

import Model.Vehicle;
import Service.IVehicleService;
import Utility.Exceptions.InternalServerErrorException;
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

    @CrossOrigin(methods = {RequestMethod.GET})
    @RequestMapping(value = "/{plate}", method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity<?> getVehicle(@PathVariable("plate") String plate) {
        try {
            Vehicle vehicle = vehicleService.getVehicleInfo(plate);
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
