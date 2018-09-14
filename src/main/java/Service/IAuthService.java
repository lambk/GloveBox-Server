package Service;

import Transfer.LoginDTO;
import Transfer.LogoutDTO;
import Transfer.TokenDTO;
import Transfer.ValidationDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;

public interface IAuthService {
    TokenDTO login(LoginDTO loginDTO) throws UnauthorizedException, InternalServerErrorException;

    void logout(LogoutDTO logoutDTO) throws UnauthorizedException;

    boolean validateToken(ValidationDTO validationDTO);
}
