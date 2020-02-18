package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.Getter;

@Getter
public class CreateUserRequestView {
    String name;
    String password;

    public User toUser(){return new User(name, password);}
}
