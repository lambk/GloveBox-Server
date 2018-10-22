package ServiceTest;

import Access.IUserAccess;
import Model.User;
import Service.UserService;
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
        User user = new User(null, "email@domain.com", "firstname", "lastname", null, "password");
        when(userAccess.getUserByEmail(user.getEmail())).thenReturn(null);
        try {
            userService.createUser(user);
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }
    }

    @Test
    public void testDuplicateUserRegistration() {
        User user = new User(null, "email@domain.com", "firstname", "lastname", null, "password");
        when(userAccess.getUserByEmail(user.getEmail())).thenReturn(new User());
        try {
            userService.createUser(user);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
    }
}
