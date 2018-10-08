package Service;

import Access.IUserAccess;
import Mappers.IMapper;
import Mappers.RegistrationMapper;
import Model.User;
import Transfer.RegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserAccess userAccess;
    private final IMapper<RegistrationDTO, User> registrationMapper;

    @Autowired
    public UserService(IUserAccess userAccess) {
        this.userAccess = userAccess;
        registrationMapper = new RegistrationMapper();
    }

    @Override
    public void test() {
        userAccess.test();
    }

    /**
     * Attempts to add a new user to the database.
     * Before insert, the following actions are taken:
     * - The database is checked for the presence of a user with the same email
     * - A random salt is generated and used to hash the password given in the Registration DTO
     * - The new User object is sent to the access layer to be inserted
     *
     * @param registrationDTO The registration details
     */
    @Override
    public void createUser(RegistrationDTO registrationDTO) throws IllegalArgumentException, InternalServerErrorException {
        if (userAccess.getUserByEmail(registrationDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Account already exists");
        }
        String salt = DigestUtils.sha256Hex(UUID.randomUUID().toString());
        String hashedPassword = DigestUtils.sha256Hex(salt + registrationDTO.getPassword());
        User user = registrationMapper.map(registrationDTO);
        user.setSalt(salt);
        user.setPassword(hashedPassword);
        userAccess.saveUser(user);
    }

    @Override
    public User getUserByID(int id) {
        return userAccess.getUserByID(id);
    }
}
