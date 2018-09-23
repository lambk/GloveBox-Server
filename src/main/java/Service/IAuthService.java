package Service;

import Model.User;
import Transfer.LoginDTO;
import Transfer.TokenDTO;
import Utility.Exceptions.InternalServerErrorException;
import Utility.Exceptions.UnauthorizedException;

public interface IAuthService {
    TokenDTO login(LoginDTO loginDTO) throws UnauthorizedException, InternalServerErrorException;

    void logout(String token);

    User getUserByToken(String token) throws IllegalArgumentException;

    boolean isTokenValid(String token);

    boolean doesTokenMatchEmail(String token, String email);
}
