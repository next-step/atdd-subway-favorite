package atdd.user.application.dto;

import atdd.user.domain.User;

public class UserResponseView {
    private Long id;
    private String email;
    private String name;
    private String password;

    public UserResponseView() {
    }

    public UserResponseView(String email, String name, String password) {
        this(0L, email, name, password);
    }

    public UserResponseView(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public static UserResponseView of(User user) {
        return new UserResponseView(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword()
        );
    }
}
