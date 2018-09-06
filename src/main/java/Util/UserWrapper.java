package Util;

import Model.User;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public class UserWrapper implements IUserWrapper {
    @Valid
    private User user;
    @Size(min = 6, max = 100)
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
