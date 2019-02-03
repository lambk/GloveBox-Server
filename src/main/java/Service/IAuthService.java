package Service;

import Transfer.LoginDTO;
import Transfer.TokenDTO;
import Utility.Exceptions.UnauthorizedException;

public interface IAuthService {
    TokenDTO login(LoginDTO loginDTO) throws UnauthorizedException;

    void logout(String token, int userID);

    boolean isTokenValid(String token, int userID);
}
