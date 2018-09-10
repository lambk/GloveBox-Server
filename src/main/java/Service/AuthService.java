package Service;

import Access.IUserAccess;
import Transfer.AuthenticationDTO;
import Transfer.LoginDTO;
import Transfer.LogoutDTO;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {

    private final IUserAccess userAccess;

    @Autowired
    public AuthService(IUserAccess userAccess) {
        this.userAccess = userAccess;
    }

    /**
     * Checks the provided login details against the record in the database. The AuthenticationDTO for that record
     * is fetched from the Access layer (if none exists, a 401 is returned), after which the provided password is hashed using the salt from the db,
     * and the result is then checked against the (hashed) password from the db.
     * <p>
     * If there is a match, a new random token is generated and inserted into the database for that record.
     * Else, a 401 is returned
     *
     * @param loginDTO The details for the login attempt
     * @return A ResponseEntity representing the outcome of the login attempt. Statuses are as follows:
     * - UNAUTHORIZED if the email/password combination doesn't match the record from the database
     * - OK if details were correct and the token was successfully added to the db
     * - INTERNAL_SERVER_ERROR if the access layer throws an SQLException on token insertion
     */
    @Override
    public ResponseEntity<String> login(LoginDTO loginDTO) {
        AuthenticationDTO authenticationObject = userAccess.getAuthenticationDetailsByEmail(loginDTO.getEmail());
        if (authenticationObject == null) {
            return new ResponseEntity<>("The provided email/password combination is incorrect", HttpStatus.UNAUTHORIZED);
        }
        String attemptedHash = DigestUtils.sha256Hex(authenticationObject.getSalt() + loginDTO.getPassword());
        if (attemptedHash.equals(authenticationObject.getPassword())) {
            String token = (DigestUtils.sha256Hex(UUID.randomUUID().toString()));
            try {
                userAccess.insertToken(loginDTO.getEmail(), token);
                return new ResponseEntity<>(token, HttpStatus.OK);
            } catch (SQLException e) {
                e.printStackTrace();
                return new ResponseEntity<>("There was an error saving the login token", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("The provided email/password combination is incorrect", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Checks the token given in the Logout Transfer object against the matching record in the database.
     * If a token from the database cannot be found or the tokens do not match, a 401 is returned
     *
     * @param logoutDTO The logout details (email, token) from the controller
     * @return A ResponseEntity representing the outcome of the logout attempt. Statuses are as follows:
     * - UNAUTHORIZED if there was no token found in the db or the tokens did not match
     * - OK if the tokens matched
     */
    @Override
    public ResponseEntity<String> logout(LogoutDTO logoutDTO) {
        String token = userAccess.getTokenByEmail(logoutDTO.getEmail());
        if (token == null || !token.equals(logoutDTO.getToken())) {
            return new ResponseEntity<>("The provided email/token combination is incorrect", HttpStatus.UNAUTHORIZED);
        }
        userAccess.deleteToken(logoutDTO.getEmail());
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }
}
