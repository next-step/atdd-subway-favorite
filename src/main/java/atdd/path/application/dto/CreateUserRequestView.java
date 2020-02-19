package atdd.path.application.dto;

import atdd.path.domain.User;

public class CreateUserRequestView {
    private String email;
    private String name;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public User toUser() {
        return new User(email, name, password);
    }
}
