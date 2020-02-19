package atdd.path.application.dto;

import atdd.path.domain.User;

public class CreateUserRequestView {
    private String name;
    private String email;
    private String password;

    public User toUser() {
       return new User(email, name, password);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
