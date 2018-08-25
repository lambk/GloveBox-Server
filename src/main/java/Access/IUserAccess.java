package Access;

import Model.User;
import Util.IUserWrapper;

import java.sql.SQLException;

public interface IUserAccess {

    User getUserByEmail(String email);

    void insertUser(IUserWrapper userWrapper, String salt) throws SQLException;
}
