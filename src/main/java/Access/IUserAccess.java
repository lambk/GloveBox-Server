package Access;

import Model.User;
import Transfer.RegistrationDTO;

import java.sql.SQLException;

public interface IUserAccess {

    User getUserByEmail(String email);

    User getAuthenticationDetailsByEmail(String email);

    String getTokenByEmail(String email);

    void insertToken(String email, String token) throws SQLException;

    void deleteToken(String token);

    void insertUser(RegistrationDTO registrationDTO) throws SQLException;
}
