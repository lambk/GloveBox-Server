package Access;

import Model.User;

import java.sql.SQLException;

public interface IUserAccess {

    User getUserByID(int id);

    User getUserByEmail(String email);

    User getUserByToken(String token);

    User getUserAuthDetails(String email);

    void insertToken(String email, String token) throws SQLException;

    void deleteToken(String token);

    void insertUser(User user) throws SQLException;
}
