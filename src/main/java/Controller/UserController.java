package Controller;

import Service.IUserService;
import Transfer.RegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
     * The UserWrapper provided is validated on receiving based on the property annotations in User and RegistrationDTO
     * @param userInfo The request body containing the user property fields, and password
     * @return The ResponseEntity outcome passed from the service layer
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDTO userInfo) {
        return userService.createUser(userInfo);
    }
}
