package Access;

import Model.User;
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
    public User getUser(String email) {
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

    @Override
    public User getUserByToken(String token) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_user_by_token(?)}");
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setEmail(resultSet.getString("email"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches the details needed for login authentication for the record with the given email
     * The authentication details include the salt and (hashed) password
     *
     * @param email The email to find records using
     * @return The User object with the salt and password fields
     */
    @Override
    public User getUserAuthDetails(String email) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_auth_by_email(?)}");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setSalt(resultSet.getString("salt"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Inserts a given token into the database for the record with the given email. Called when
     * a user has successfully logged in
     *
     * @param email The email to find which record to add the token under
     * @param token The new token
     * @throws SQLException If there was an error performing the operation
     */
    @Override
    public void insertToken(String email, String token) throws SQLException {
        Connection connection = factory.getConnection();
        PreparedStatement statement = connection.prepareStatement("{CALL add_token_for_email(?, ?)}");
        statement.setString(1, email);
        statement.setString(2, token);
        statement.executeUpdate();
    }

    /**
     * Deletes the token for the given email
     *
     * @param token the token to clear
     */
    @Override
    public void deleteToken(String token) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL delete_token(?)}");
            statement.setString(1, token);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempts to insert a user into the database. These details are then used to
     * call the insert_user stored procedure.
     *
     * @param user The new User information
     * @throws SQLException Used to provide feedback on the outcome to the service layer
     */
    @Override
    public void insertUser(User user) throws SQLException {
        Connection connection = factory.getConnection();
        PreparedStatement statement = connection.prepareStatement("{CALL insert_user(?, ?, ?, ?, ?)}");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getSalt());
        statement.setString(5, user.getPassword());
        statement.executeUpdate();
    }
}
