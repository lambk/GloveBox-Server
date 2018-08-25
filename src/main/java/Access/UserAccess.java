package Access;

import Model.User;
import Util.IUserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserAccess implements IUserAccess {

    private final ConnectionFactory factory;

    @Autowired
    public UserAccess(ConnectionFactory factory) {
        this.factory = factory;
    }

    /**
     * Fetches a user record from the database that has the given email.
     * Returns a new User object if a record is found, else returns null
     *
     * @param email The email to find records using
     * @return A matching User record if found, else null
     */
    @Override
    public User getUserByEmail(String email) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_user_by_email(?)}");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("email"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Attempts to insert a user into the database. The user is passed in a UserWrapper
     * object, which holds the (hashed) password from the service layer. These details are then used to
     * call the insert_user stored procedure.
     *
     * @param userWrapper The UserWrapper containing User and password information
     * @param salt The generated salt (generated in the service layer)
     * @throws SQLException Used to provide feedback on the outcome to the service layer
     */
    @Override
    public void insertUser(IUserWrapper userWrapper, String salt) throws SQLException {
        Connection connection = factory.getConnection();
        PreparedStatement statement = connection.prepareStatement("{CALL insert_user(?, ?, ?, ?, ?)}");
        statement.setString(1, userWrapper.getUser().getEmail());
        statement.setString(2, userWrapper.getUser().getFirstName());
        statement.setString(3, userWrapper.getUser().getLastName());
        statement.setString(4, salt);
        statement.setString(5, userWrapper.getPassword());
        statement.executeUpdate();
    }
}
