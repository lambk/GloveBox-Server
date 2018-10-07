package ServiceTest;

import Access.IUserAccess;
import Model.User;
import Service.AuthService;
import Transfer.LoginDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @Mock
    private IUserAccess userAccess;

    @InjectMocks
    private AuthService authService;

    private User authenticationDetails;

    @Before
    public void setUp() {
        authenticationDetails = new User();
        authenticationDetails.setId(1);
        authenticationDetails.setSalt("salt");
        authenticationDetails.setPassword("13601bda4ea78e55a07b98866d2be6be0744e3866f13c00c811cab608a28f322"); //"salt" + "password" hashed
    }

    @Test
    public void testSuccessfulLogin() {
        LoginDTO loginDTO = new LoginDTO("email@domain.com", "password");
        when(userAccess.getUserAuthDetails(loginDTO.getEmail())).thenReturn(authenticationDetails);
        try {
            authService.login(loginDTO);
        } catch (UnauthorizedException | InternalServerErrorException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUnsuccessfulLoginDueToPassword() {
        LoginDTO loginDTO = new LoginDTO("email@domain.com", "p4ssw0rd");
        when(userAccess.getUserAuthDetails(loginDTO.getEmail())).thenReturn(authenticationDetails);
        try {
            authService.login(loginDTO);
            Assert.fail();
        } catch (InternalServerErrorException e) {
            Assert.fail();
        } catch (UnauthorizedException ignored) {
        }
    }

    @Test
    public void testUnsuccessfulLoginDueToEmail() {
        LoginDTO loginDTO = new LoginDTO("bob@domain.com", "password");
        try {
            authService.login(loginDTO);
            Assert.fail();
        } catch (InternalServerErrorException e) {
            Assert.fail();
        } catch (UnauthorizedException ignored) {
        }
    }

    @Test
    public void testTokenCheck() {
        String loginToken = "token";
        int id = 1;
        when(userAccess.getUserByToken(loginToken)).thenReturn(getUserTokenMock(id));
        Assert.assertTrue(authService.isTokenValid(loginToken, id));
    }

    @Test
    public void testTokenCheckIDMismatch() {
        String loginToken = "token";
        int id = 1;
        when(userAccess.getUserByToken(loginToken)).thenReturn(getUserTokenMock(id + 1));
        Assert.assertFalse(authService.isTokenValid(loginToken, id));
    }

    @Test
    public void testTokenCheckTokenMismatch() {
        String loginToken = "token";
        int id = 1;
        when(userAccess.getUserByToken(loginToken)).thenReturn(null);
        Assert.assertFalse(authService.isTokenValid(loginToken, id));
    }

    /**
     * Generates a user object to return from the mocked method
     *
     * @param id The generated user's id
     * @return The generates user object
     */
    private User getUserTokenMock(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

}