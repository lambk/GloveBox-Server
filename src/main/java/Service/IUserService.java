package Service;

import Transfer.RegistrationDTO;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    ResponseEntity<String> createUser(RegistrationDTO registrationDTO);
}
