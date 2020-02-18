package atdd.path.application.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CreateUserRequestView {
    String email;
    String name;
    String password;

    @Builder
    public CreateUserRequestView(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() { return email; }

    public String getName() { return name; }

    public String getPassword() { return password; }
}
