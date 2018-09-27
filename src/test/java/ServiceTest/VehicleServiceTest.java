package ServiceTest;

import Access.IVehicleAccess;
import Model.User;
import Model.Vehicle;
import Service.IAuthService;
import Service.VehicleService;
import Transfer.VehicleRegistrationDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceTest {

    @Mock
    private IVehicleAccess vehicleAccess;

    @Mock
    private IAuthService authService;

    @InjectMocks
    private VehicleService vehicleService;

    private User signedInUser;
    private String loginToken;

    @Before
    public void setUp() {
        signedInUser = new User(1, "email@domain.com", "First", "Last");
        loginToken = "Token";
        when(authService.getUserByToken(loginToken)).thenReturn(signedInUser);
    }

    @Test
    public void testDuplicateVehicleRegistration() {
        when(vehicleAccess.getVehicle("ABC123", signedInUser.getId())).thenReturn(new Vehicle());
        VehicleRegistrationDTO vehicleRegistration1 = new VehicleRegistrationDTO("ABC123", "Toyota", "Corolla", 2000, 10000, LocalDate.now(), "New Zealand");
        VehicleRegistrationDTO vehicleRegistration2 = new VehicleRegistrationDTO("ABC123", "Honda", "Integra", 2000, 10000, LocalDate.now(), "New Zealand");
        try {
            vehicleService.registerVehicle(vehicleRegistration1, loginToken);
            vehicleService.registerVehicle(vehicleRegistration2, loginToken);
        } catch (UnauthorizedException | InternalServerErrorException e) {
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testSuccessfulVehicleRegistration() {
        VehicleRegistrationDTO vehicleRegistration1 = new VehicleRegistrationDTO("ABC123", "Toyota", "Corolla", 2000, 10000, LocalDate.now(), "New Zealand");
        try {
            vehicleService.registerVehicle(vehicleRegistration1, loginToken);
        } catch (UnauthorizedException | InternalServerErrorException | IllegalArgumentException e) {
            Assert.fail();
        }
    }

    @Test
    public void testVehicleRegistrationWhenLoggedOut() {
        VehicleRegistrationDTO vehicleRegistration1 = new VehicleRegistrationDTO("ABC123", "Toyota", "Corolla", 2000, 10000, LocalDate.now(), "New Zealand");
        try {
            vehicleService.registerVehicle(vehicleRegistration1, "A different token");
            Assert.fail();
        } catch (InternalServerErrorException e) {
            Assert.fail();
        } catch (UnauthorizedException ignored) {
        }
    }

    @Test
    public void testGettingExistingVehicle() {
        Vehicle returned = new Vehicle("ABC123", "Toyota", "Celica", 1994, 200000, LocalDate.now(), "New Zealand");
        when(vehicleAccess.getVehicle("ABC123", signedInUser.getId())).thenReturn(returned);
        Vehicle fetched = vehicleAccess.getVehicle("ABC123", signedInUser.getId());
        Assert.assertEquals(returned, fetched);
    }

    @Test
    public void testGettingNonexistingVehicle() {
        when(vehicleAccess.getVehicle("ABC123", signedInUser.getId())).thenReturn(null);
        Vehicle fetched = vehicleAccess.getVehicle("ABC123", signedInUser.getId());
        Assert.assertNull(fetched);
    }
}
