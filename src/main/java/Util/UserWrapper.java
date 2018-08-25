package Util;

import Model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class UserWrapper implements IUserWrapper {
    @Valid
    private User user;
    @NotEmpty
    private String password;
    private String salt;

    public UserWrapper() {}

    public User getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
