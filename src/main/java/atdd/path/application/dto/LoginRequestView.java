package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestView {
    private String email;
    private String password;

    @Builder
    public LoginRequestView(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
