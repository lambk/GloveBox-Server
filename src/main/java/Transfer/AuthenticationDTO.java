package Transfer;

public class AuthenticationDTO {
    private final String salt;
    private final String password;

    public AuthenticationDTO(String salt, String password) {
        this.salt = salt;
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }

}
