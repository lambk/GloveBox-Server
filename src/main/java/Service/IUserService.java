package Service;

import Model.User;
import Transfer.RegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;

public interface IUserService {

    void test();

    void createUser(RegistrationDTO registrationDTO) throws IllegalArgumentException, InternalServerErrorException;

    User getUserByID(int id);
}
