package Service;

import Util.IUserWrapper;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    ResponseEntity<String> createUser(IUserWrapper userWrapper);
}
