package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestView {
    private String email;
    private String name;
    private String password;

    @Builder
    public UserRequestView(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User toUser() {
        return User.createBuilder()
                .email(this.email)
                .name(this.name)
                .password(this.password)
                .build();
    }
}
