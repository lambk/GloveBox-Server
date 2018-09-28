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
    public void testUnsuccessfulLogin() {
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

}