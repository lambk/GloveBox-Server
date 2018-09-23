package Controller;

import Service.IAuthService;
import Transfer.LoginDTO;
import Transfer.TokenDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
     * @return The ResponseEntity outcome
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            TokenDTO authentication = authService.login(loginDTO);
            return new ResponseEntity<>(authentication, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Calls the service layer to attempt to logout using the LogoutDTO provided in the request body
     *
     * @param token The current token stored on the client
     * @return The ResponseEntity outcome
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> logout(@RequestHeader("token") String token) {
        authService.logout(token);
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    /**
     * Calls the service layer to validate the given token against the token in the database
     *
     * @param token The current token stored on the client
     * @return The ResponseEntity outcome
     */
    @CrossOrigin(methods = {RequestMethod.POST})
    @RequestMapping(value = "/validate", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> validate(@RequestHeader("token") String token) {
        if (authService.isTokenValid(token)) {
            return new ResponseEntity<>("Token is valid", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Token is invalid", HttpStatus.UNAUTHORIZED);
        }
    }
}
