package Service;

import Transfer.LoginDTO;
import Transfer.LogoutDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<String> login(LoginDTO loginDTO);

    ResponseEntity<String> logout(LogoutDTO logoutDTO);
}
