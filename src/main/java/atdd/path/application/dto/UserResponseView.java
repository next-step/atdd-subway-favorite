package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseView {
    Long id;
    String email;
    String name;
    String password;

    @Builder
    public UserResponseView(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public static UserResponseView of(User user) {
        return new UserResponseView(user.getId(), user.getEmail(), user.getName(), user.getPassword());
    }

}
