package atdd.path.application.dto;

import atdd.path.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NotNull(groups = String.class)
public class UserRequestView {

    @Email
    private String email;
    private String name;
    private String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .name(name)
                .password(password )
                .build();
    }
}
