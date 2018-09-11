package Service;

import Access.IUserAccess;
import Transfer.RegistrationDTO;
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
     * - A random salt is generated and used to hash the password in the Registration DTO
     * - The Registration DTO is sent to the access layer to be inserted
     *
     * @param  registrationDTO The DTO containing the User object, and the desired password
     * @return A ResponseEntity representing the outcome of the creation
     */
    @Override
    public ResponseEntity<String> createUser(RegistrationDTO registrationDTO) {
        if (userAccess.getUserByEmail(registrationDTO.getUser().getEmail()) != null) {
            return new ResponseEntity<>(String.format("Account %s already exists", registrationDTO.getUser().getEmail()), HttpStatus.BAD_REQUEST);
        }
        registrationDTO.setSalt(DigestUtils.sha256Hex(UUID.randomUUID().toString()));
        registrationDTO.setPassword(DigestUtils.sha256Hex(registrationDTO.getSalt() + registrationDTO.getPassword()));
        try {
            userAccess.insertUser(registrationDTO);
            return new ResponseEntity<>(String.format("Account %s created", registrationDTO.getUser().getEmail()), HttpStatus.CREATED);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Account could not be created", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
