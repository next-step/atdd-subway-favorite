package nextstep.subway.auth.domain;

import java.util.Objects;

public class UserDetails {
    protected Long id;
    protected String email;
    protected String password;
    protected Integer age;

    public UserDetails() {
    }

    public UserDetails(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }
}

