package Controller;

import Service.IUserService;
import Transfer.RegistrationDTO;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
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

    /**
     * Calls the service layer to attempt to create the given user.
     *
     * @param userInfo The request body containing the user property fields, and password
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
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
