package Service;

import Transfer.RegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;

public interface IUserService {
    void createUser(RegistrationDTO registrationDTO) throws IllegalArgumentException, InternalServerErrorException;
}
