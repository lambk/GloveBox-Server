package Transfer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@SuppressWarnings("unused")
public class RegistrationDTO {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Size(min = 6, max = 100)
    private String password;

    public RegistrationDTO() {}

    public RegistrationDTO(@NotEmpty @Email String email, @NotEmpty String firstName, @NotEmpty String lastName, @Size(min = 6, max = 100) String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
