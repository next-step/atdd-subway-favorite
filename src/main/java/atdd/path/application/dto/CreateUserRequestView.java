package atdd.path.application.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CreateUserRequestView {
    Long id;
    String email;
    String name;
    String password;

    @Builder
    public CreateUserRequestView(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Long getId() { return id; }

    public String getEmail() { return email; }

    public String getName() { return name; }

    public String getPassword() { return password; }
}
