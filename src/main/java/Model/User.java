package Model;

import javax.validation.constraints.NotEmpty;

public class User {
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstName;
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
