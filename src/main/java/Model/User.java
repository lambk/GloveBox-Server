package Model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class User {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    public User() {}

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
