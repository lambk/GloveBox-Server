package Util;

import Model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class UserWrapper implements IUserWrapper {
    @Valid
    private User user;
    @NotEmpty
    private String password;

    public UserWrapper() {}

    public User getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
