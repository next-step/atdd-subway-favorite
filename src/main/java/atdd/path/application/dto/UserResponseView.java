package atdd.path.application.dto;

import atdd.path.domain.User;

import java.io.Serializable;

public class UserResponseView implements Serializable {
    private static final long serialVersionUID = 8806009416105685525L;

    private Long id;
    private String email;
    private String name;

    public UserResponseView() {
    }

    public UserResponseView(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserResponseView of(User user) {
        return new UserResponseView(user.getId(), user.getEmail(), user.getName());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
