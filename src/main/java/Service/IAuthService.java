package Service;

import Transfer.LoginDTO;
import Transfer.TokenDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;

public interface IAuthService {
    TokenDTO login(LoginDTO loginDTO) throws UnauthorizedException, InternalServerErrorException;

    void logout(String email, String token) throws UnauthorizedException;

    boolean isTokenValid(String email, String token);
}
