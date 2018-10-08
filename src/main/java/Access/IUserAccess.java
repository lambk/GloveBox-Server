package Access;

import Model.User;

public interface IUserAccess {

    void test();

    User getUserByID(int id);

    User getUserByEmail(String email);

    void saveUser(User user);

    void updateUser(User user);
}
