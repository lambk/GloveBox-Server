package Service;

import Access.IUserAccess;
import Model.User;
import Transfer.RegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Checks if the user can be created.
     * The following actions are taken:
     * - The database is checked for the presence of a user with the same email
     * - A random salt is generated and used to hash the password given in the Registration DTO
     * - The new User object is sent to the access layer to be inserted
     *
     * @param registrationDTO The DTO containing the registration details
     */
    @Override
    public void createUser(RegistrationDTO registrationDTO) throws IllegalArgumentException, InternalServerErrorException {
        if (userAccess.getUser(registrationDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Account already exists");
        }
        String salt = DigestUtils.sha256Hex(UUID.randomUUID().toString());
        String hashedPassword = DigestUtils.sha256Hex(salt + registrationDTO.getPassword());
        User user = new User(registrationDTO.getEmail(), registrationDTO.getFirstName(), registrationDTO.getLastName(),
                salt, hashedPassword);
        try {
            userAccess.insertUser(user);
        } catch (SQLException e) {
            throw new InternalServerErrorException("Account could not be created");
        }
    }
}
