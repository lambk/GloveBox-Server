package Controller;

import Service.IUserService;
import Transfer.RegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users", consumes = "application/json")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(methods = {RequestMethod.GET})
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<String> test() {
        userService.test();
        return new ResponseEntity<>("k lol", HttpStatus.OK);
    }

    /**
     * Calls the service layer to attempt to create the given user.
     *
     * @param userInfo The request body containing the registration details
     * @return The ResponseEntity outcome
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDTO userInfo) {
        try {
            userService.createUser(userInfo);
            return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
