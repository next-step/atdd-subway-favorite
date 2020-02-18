package atdd.path.application.dto;

import atdd.path.domain.Station;
import atdd.path.domain.User;

public class CreateUserRequestView {
    String email;
    String name;
    String password;

    public User toUser() {
        return new User(email, name, password);
    }
}
