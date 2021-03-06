package ServiceTest;

import Access.IUserAccess;
import Model.User;
import Service.UserService;
import Transfer.RegistrationDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private IUserAccess userAccess;

    @InjectMocks
    private UserService userService;

    @Test
    public void testValidUserRegistration() {
        RegistrationDTO registrationDTO = new RegistrationDTO("email@domain.com", "firstname", "lastname", "password");
        when(userAccess.getUserByEmail(registrationDTO.getEmail())).thenReturn(null);
        try {
            userService.createUser(registrationDTO);
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }
    }

    @Test
    public void testDuplicateUserRegistration() {
        RegistrationDTO registration = new RegistrationDTO("email@domain.com", "firstname", "lastname", "password");
        when(userAccess.getUserByEmail(registration.getEmail())).thenReturn(new User());
        try {
            userService.createUser(registration);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
    }
}
