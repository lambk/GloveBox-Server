package Service;

import Access.IUserAccess;
import Model.User;
import Transfer.LoginDTO;
import Transfer.TokenDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Checks the provided login details against the record in the database. The User authentication for that record
     * is fetched from the Access layer (if none exists, a 401 is returned), after which the provided password is hashed using the salt from the db,
     * and the result is then checked against the (hashed) password from the db.
     * <p>
     * If there is a match, a new random token is generated and inserted into the database for that record.
     * Else, a 401 is returned
     *
     * @param loginDTO The details for the login attempt
     * @return A ResponseEntity representing the outcome of the login attempt
     */
    @Override
    public TokenDTO login(LoginDTO loginDTO) throws UnauthorizedException, InternalServerErrorException {
        User authentication = userAccess.getUserAuthDetails(loginDTO.getEmail());
        if (authentication == null) {
            throw new UnauthorizedException("The provided email/password combination is incorrect");
        }
        String attemptedHash = DigestUtils.sha256Hex(authentication.getSalt() + loginDTO.getPassword());
        if (!attemptedHash.equals(authentication.getPassword())) {
            throw new UnauthorizedException("The provided email/password combination is incorrect");
        } else {
            String token = (DigestUtils.sha256Hex(UUID.randomUUID().toString()));
            try {
                userAccess.insertToken(loginDTO.getEmail(), token);
                return new TokenDTO(loginDTO.getEmail(), token);
            } catch (SQLException e) {
                throw new InternalServerErrorException("There was an error saving the login token");
            }
        }
    }

    /**
     * Removes the token for the user with the given token
     *
     * @param token The applications current token
     */
    @Override
    public void logout(String token) {
        userAccess.deleteToken(token);
    }

    /**
     * Checks the given token against the database to see if the token exists
     * Returns HTTP OK or HTTP Unauthorised depending on whether the given token is up to date or not (respectively)
     *
     * @param token The applications current token
     * @return Boolean stating whether the token is valid
     */
    @Override
    public boolean isTokenValid(String token) {
        return userAccess.getUserByToken(token) != null;
    }
}
