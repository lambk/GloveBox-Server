package Access;

import Model.User;

import java.sql.SQLException;

public interface IUserAccess {

    User getUser(String email);

    User getAuthenticationDetails(String email);

    String getTokenDetails(String email);

    void insertToken(String email, String token) throws SQLException;

    void deleteToken(String token);

    void insertUser(User user) throws SQLException;
}
