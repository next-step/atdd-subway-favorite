package atdd.path.domain;

public class User {
    private Long id;

    String email;
    String name;
    String password;

    public User() {
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User(Long userId, String userEmail, String userName, String userPassword) {
        this.id = userId;
        this.email = userEmail;
        this.name = userName;
        this.password = userPassword;
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
}
