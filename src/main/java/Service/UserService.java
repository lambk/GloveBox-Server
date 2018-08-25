package Service;

import Access.IUserAccess;
import Util.IUserWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserAccess userAccess;

    @Autowired
    public UserService(IUserAccess userAccess) {
        this.userAccess = userAccess;
    }

    /**
     * Performs business logic to check if the user can be created.
     * The following actions are taken:
     * - The database is checked for the presence of a user with the same email
     * - A random salt is generated and used to hash the password in the UserWrapper
     * - The UserWrapper is sent to the access layer to be inserted
     *
     * @param userWrapper The wrapper containing the User object, and the desired password
     * @return A ResponseEntity representing the outcome of the creation. Statuses are as follows:
     * - BAD_REQUEST if the email is already used
     * - CREATED if the access layer performs an insert without throwing an SQLException
     * - INTERNAL_SERVER_ERROR if the access layer throws an SQLException
     */
    @Override
    public ResponseEntity<String> createUser(IUserWrapper userWrapper) {
        if (userAccess.getUserByEmail(userWrapper.getUser().getEmail()) != null) {
            return new ResponseEntity<>(String.format("Account %s already exists", userWrapper.getUser().getEmail()), HttpStatus.BAD_REQUEST);
        }
        String salt = DigestUtils.sha256Hex(UUID.randomUUID().toString());
        userWrapper.setPassword(DigestUtils.sha256Hex(salt + userWrapper.getPassword()));
        try {
            userAccess.insertUser(userWrapper, salt);
            return new ResponseEntity<>(String.format("Account %s created", userWrapper.getUser().getEmail()), HttpStatus.CREATED);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Account could not be created", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
