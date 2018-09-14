package Service;

import Transfer.RegistrationDTO;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

public interface IUserService {
    void createUser(RegistrationDTO registrationDTO) throws IllegalArgumentException, InternalException;
}
