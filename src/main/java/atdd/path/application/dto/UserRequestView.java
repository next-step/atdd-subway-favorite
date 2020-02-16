package atdd.path.application.dto;

import atdd.path.domain.User;

import javax.validation.constraints.Email;

public class UserRequestView {

    @Email
    private String email;
    private String name;
    private String password;

    public User toEntity() {
        return User.builder()
                .build();
    }
}
