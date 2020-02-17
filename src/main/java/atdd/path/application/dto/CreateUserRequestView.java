package atdd.path.application.dto;

import lombok.Builder;

public class CreateUserRequestView {
    private String name;
    private String email;
    private String password;

    @Builder
    private CreateUserRequestView(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
