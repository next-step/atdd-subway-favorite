package atdd.path.application.dto;

import atdd.path.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginRequestView {
    private String name;
    @Getter
    private String password;
    private String email;

    @Builder
    public LoginRequestView(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User toUser() {
        return User.builder()
                .name(this.name)
                .password(this.password)
                .email(this.email).build();
    }
}
