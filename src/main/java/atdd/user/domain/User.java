package atdd.user.domain;

import java.util.Objects;

public class User {

    public static final String TABLE_NAME = "USERS";

    private Long id;
    private Email email;
    private String name;
    private String password;

    private User() { }

    public static User create(Email email, String name, String password) {
        return of(null, email, name, password);
    }

    public static User of(Long id, Email email, String name, String password) {
        User user = new User();
        user.id = id;
        user.email = email;
        user.name = name;
        user.password = password;
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getEmailAddress();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
