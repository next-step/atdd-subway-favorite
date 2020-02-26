package atdd.security;

import atdd.user.domain.User;

import java.util.Objects;

public class LoginUserInfo {

    private Long id;
    private String email;
    private String name;
    private String password;

    private LoginUserInfo() { }

    public static LoginUserInfo from(User user) {
        return of(user.getId(), user.getEmail(), user.getName(), user.getPassword());
    }

    public static LoginUserInfo of(Long id, String email, String name, String password) {
        LoginUserInfo loginUser = new LoginUserInfo();
        loginUser.id = id;
        loginUser.email = email;
        loginUser.name = name;
        loginUser.password = password;
        return loginUser;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginUserInfo)) return false;
        LoginUserInfo loginUser = (LoginUserInfo) o;
        return Objects.equals(id, loginUser.id) &&
                Objects.equals(email, loginUser.email) &&
                Objects.equals(name, loginUser.name) &&
                Objects.equals(password, loginUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password);
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
