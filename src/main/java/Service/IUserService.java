package Service;

import Model.User;
import Transfer.RegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;

public interface IUserService {

    User getUserByToken(String token) throws IllegalArgumentException;

    void createUser(RegistrationDTO registrationDTO) throws IllegalArgumentException, InternalServerErrorException;
}
