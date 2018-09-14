package Access;

import Model.User;
import Transfer.RegistrationDTO;
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
     * Fetches the details needed for login authentication for the record with the given email
     * The authentication details include the salt and (hashed) password
     *
     * @param email The email to find records using
     * @return The User object with the salt and password fields
     */
    @Override
    public User getAuthenticationDetailsByEmail(String email) {
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
     * Fetches the token for the record with the given email. This is needed in the AuthService to check
     * that the user has the latest token when attempting to log out
     *
     * @param email The email to find records using
     * @return The token (String) for that email
     */
    @Override
    public String getTokenByEmail(String email) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_token_by_email(?)}");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("token");
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
     * @param email The email to find which record to delete the token under
     */
    @Override
    public void deleteToken(String email) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL delete_token_for_email(?)}");
            statement.setString(1, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempts to insert a user into the database. The user is passed in a UserWrapper
     * object, which holds the (hashed) password from the service layer. These details are then used to
     * call the insert_user stored procedure.
     *
     * @param registrationDTO The Registration DTO containing User, salt and password information
     * @throws SQLException Used to provide feedback on the outcome to the service layer
     */
    @Override
    public void insertUser(RegistrationDTO registrationDTO) throws SQLException {
        Connection connection = factory.getConnection();
        PreparedStatement statement = connection.prepareStatement("{CALL insert_user(?, ?, ?, ?, ?)}");
        statement.setString(1, registrationDTO.getUser().getEmail());
        statement.setString(2, registrationDTO.getUser().getFirstName());
        statement.setString(3, registrationDTO.getUser().getLastName());
        statement.setString(4, registrationDTO.getSalt());
        statement.setString(5, registrationDTO.getPassword());
        statement.executeUpdate();
    }
}
