package atdd.user.application.dto;

import atdd.user.domain.User;
import lombok.Getter;

@Getter
public class
CreateUserRequestView {
    String name;
    String password;
    String email;

    public User toUser(){return new User(name, password, email);}
}
