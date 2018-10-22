package Service;

import Model.User;

public interface IUserService {

    void createUser(User user) throws IllegalArgumentException;

    User getUserByID(int id);
}
