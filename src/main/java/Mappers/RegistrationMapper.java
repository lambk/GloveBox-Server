package Mappers;

import Model.User;
import Transfer.RegistrationDTO;

public class RegistrationMapper implements IMapper<RegistrationDTO, User> {
    @Override
    public User map(RegistrationDTO source) {
        User user = new User();
        user.setEmail(source.getEmail());
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        return user;
    }
}
