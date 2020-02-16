package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.Getter;

@Getter
public class UserResponseView {
    Long id;
    String email;
    String name;
    String password;

    public UserResponseView(Long id, String email, String name, String password) {
    }

    public static UserResponseView of(User user) {
        return new UserResponseView(user.getId(), user.getEmail(), user.getName(), user.getPassword());
    }

}
