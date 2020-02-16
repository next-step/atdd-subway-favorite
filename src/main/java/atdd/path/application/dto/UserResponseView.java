package atdd.path.application.dto;

import atdd.path.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponseView {
    private Long id;
    private String name;
    private String email;

    public UserResponseView() {
    }

    public UserResponseView(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    public static UserResponseView of(User user) {
        return new UserResponseView(user.getId(), user.getName(), user.getEmail());
    }

    public static List<UserResponseView> listOf(List<User> users) {
        return users.stream()
                .map(it -> new UserResponseView(it.getId(), it.getName(), it.getEmail()))
                .collect(Collectors.toList());
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
