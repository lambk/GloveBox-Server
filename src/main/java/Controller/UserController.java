package Controller;

import Service.IUserService;
import Util.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * The UserWrapper provided is validated on receiving based on the property annotations in User and UserWrapper
     * @param userInfo The request body containing the user property fields, and password
     * @return The ResponseEntity outcome passed from the service layer
     */
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> test(@Valid @RequestBody UserWrapper userInfo) {
        return userService.createUser(userInfo);
    }
}
