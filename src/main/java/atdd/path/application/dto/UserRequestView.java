package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
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
}
