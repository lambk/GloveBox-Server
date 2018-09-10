package Access;

import Model.User;
import Transfer.AuthenticationDTO;
import Transfer.RegistrationDTO;

import java.sql.SQLException;

public interface IUserAccess {

    User getUserByEmail(String email);

    AuthenticationDTO getAuthenticationDetailsByEmail(String email);

    String getTokenByEmail(String email);

    void insertToken(String email, String token) throws SQLException;

    void deleteToken(String token);

    void insertUser(RegistrationDTO registrationDTO) throws SQLException;
}
