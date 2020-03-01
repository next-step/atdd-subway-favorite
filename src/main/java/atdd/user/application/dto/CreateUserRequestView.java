package atdd.user.application.dto;

import atdd.user.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateUserRequestView {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 2)
    private String name;

    @NotBlank
    @Size(min = 6)
    private String password;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public CreateUserRequestView() {
    }

    public CreateUserRequestView(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User toEntity() {
        return new User(email, name, password);
    }
}
