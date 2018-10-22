package Service;

import Access.IUserAccess;
import Model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserAccess userAccess;

    @Autowired
    public UserService(IUserAccess userAccess) {
        this.userAccess = userAccess;
    }

    /**
     * Attempts to add a new user to the database.
     * Before insert, the following actions are taken:
     * - The database is checked for the presence of a user with the same email
     * - A random salt is generated and used to hash the password given in the Registration DTO
     * - The new User object is sent to the access layer to be inserted
     *
     * @param user The new user
     */
    @Override
    public void createUser(User user) throws IllegalArgumentException {
        if (userAccess.getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Account already exists");
        }
        String salt = DigestUtils.sha256Hex(UUID.randomUUID().toString());
        String hashedPassword = DigestUtils.sha256Hex(salt + user.getPassword());
        user.setSalt(salt);
        user.setPassword(hashedPassword);
        userAccess.saveUser(user);
    }

    @Override
    public User getUserByID(int id) {
        return userAccess.getUserByID(id);
    }
}
