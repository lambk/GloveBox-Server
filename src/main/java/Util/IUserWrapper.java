package Util;

import Model.User;

public interface IUserWrapper {

    User getUser();

    String getPassword();

    String getSalt();

    void setSalt(String salt);
}
