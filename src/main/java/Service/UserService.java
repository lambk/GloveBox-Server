package Service;

import Util.IUserWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserService implements IUserService {

    public ResponseEntity<String> createUser(IUserWrapper userWrapper) {
        //todo Check if user with email already exists
        return new ResponseEntity<>(String.format("Account %s created", userWrapper.getUser().getEmail()), HttpStatus.CREATED);
    }
}
