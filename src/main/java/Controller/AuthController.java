package Controller;

import Service.IAuthService;
import Transfer.LoginDTO;
import Transfer.LogoutDTO;
import Transfer.ValidationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", consumes = "application/json")
public class AuthController {

    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Calls the service layer to attempt to login using the LoginDTO provided in the request body
     *
     * @param loginDTO The login details from the client
     * @return The ResponseEntity outcome passed from the service layer
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    /**
     * Calls the service layer to validate the given token against the token in the database
     *
     * @param validationDTO The validation transfer object from the client
     * @return The ResponseEntity outcome passed
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "/validate", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> validate(@RequestBody ValidationDTO validationDTO) {
        return authService.validateToken(validationDTO);
    }

    /**
     * Calls the service layer to attempt to logout using the LogoutDTO provided in the request body
     *
     * @param logoutDTO The logout details from the client
     * @return The ResponseEntity outcome passed from the service layer
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> logout(@RequestBody LogoutDTO logoutDTO) {
        return authService.logout(logoutDTO);
    }
}
