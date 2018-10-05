package Service;

import Transfer.LoginDTO;
import Transfer.TokenDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;

public interface IAuthService {
    TokenDTO login(LoginDTO loginDTO) throws UnauthorizedException, InternalServerErrorException;

    void logout(String token);

    boolean isTokenValid(String token, int userID);
}
