package Service;

import Access.IUserAccess;
import Util.IUserWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.UUID;

@Component
public class UserService implements IUserService {

    private final IUserAccess userAccess;

    @Autowired
    public UserService(IUserAccess userAccess) {
        this.userAccess = userAccess;
    }

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
