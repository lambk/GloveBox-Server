package Transfer;

import Model.User;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public class RegistrationDTO {
    @Valid
    private User user;
    private String salt;
    @Size(min = 6, max = 100)
    private String password;

    public RegistrationDTO() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
