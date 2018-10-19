package Service;

import Model.User;
import Transfer.RegistrationDTO;

public interface IUserService {

    void createUser(RegistrationDTO registrationDTO) throws IllegalArgumentException;

    User getUserByID(int id);
}
