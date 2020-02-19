package atdd.path.application.dto;

import atdd.path.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import javax.validation.constraints.Email;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateUserRequestView {
    private String name;
    @Email
    private String email;
    private String password;

    public CreateUserRequestView() {
    }

    @Builder
    private CreateUserRequestView(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User toUSer() {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password).build();
    }
}
