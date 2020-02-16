package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.Builder;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import javax.validation.constraints.*;

public class CreateUserRequestView {
    private String email;

    private String name;

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
