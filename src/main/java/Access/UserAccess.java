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
     * Fetches the user from the database with the given id.
     * Returns a user object with the id, email, firstname, and lastname. All other fields are null
     * If no user has the given id, null is returned
     *
     * @param id The id to use to find the user
     * @return The matching user if found, else null
     */
    @Override
    public User getUserByID(int id) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_user_by_id(?)}");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        null,
                        null);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches the user from the database with the given email.
     * Returns a user object with the id, email, firstname, and lastname. All other fields are null
     * If no user has the given email, null is returned
     *
     * @param email The email to use to find the user
     * @return The matching user if found, else null
     */
    @Override
    public User getUserByEmail(String email) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_user_by_email(?)}");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        null,
                        null);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches the user from the database that currently has the given token.
     * Returns a user object with the id and email. All other fields are null.
     * If no user has the given token, null is returned
     *
     * @param token The token to use to find the user
     * @return The matching user if found, else null
     */
    @Override
    public User getUserByToken(String token) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_user_by_token(?)}");
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
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
     * Fetches the user from the database with the given email.
     * Returns a user object with only the id, salt and password field set. All other fields are null.
     * If no user has the given email, null is returned
     * <p>
     * The returned user object is used for verifying an attempted login
     *
     * @param email The email to use to find the user
     * @return The matching user if found, else null
     */
    @Override
    public User getUserAuthDetails(String email) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL get_user_auth(?)}");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
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
     * Inserts a given token into the database for the user with the given email.
     * Called when a user has successfully logged in
     *
     * @param email The email to find which user to add the token for
     * @param token The token to insert
     * @throws SQLException If there was an error with the sql operation
     */
    @Override
    public void insertToken(String email, String token) throws SQLException {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL add_token(?, ?)}");
            statement.setString(1, email);
            statement.setString(2, token);
            statement.executeUpdate();
        }
    }

    /**
     * Removes the token for the given email.
     * If no user has the given token, the database remains unchanged, and the method gives no error feedback to
     * its caller
     *
     * @param token the token to find which user to remove the token for
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
     * Attempts to insert a user into the database.
     * The salt should already be generated, and the password hashed prior to this operation
     *
     * @param user The new User information
     * @throws SQLException If there was an error with the sql operation
     */
    @Override
    public void insertUser(User user) throws SQLException {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("{CALL insert_user(?, ?, ?, ?, ?)}");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getSalt());
            statement.setString(5, user.getPassword());
            statement.executeUpdate();
        }
    }
}
