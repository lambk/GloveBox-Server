package Mappers;

import Model.User;
import Transfer.RegistrationDTO;

public class RegistrationMapper implements IMapper<RegistrationDTO, User> {
    @Override
    public User map(RegistrationDTO source) {
        return new User(null, source.getEmail(), source.getFirstName(), source.getLastName(), null, source.getPassword());
    }
}
