package Util;

import Model.User;

public interface IUserWrapper {

    User getUser();

    String getPassword();

    void setPassword(String password);
}