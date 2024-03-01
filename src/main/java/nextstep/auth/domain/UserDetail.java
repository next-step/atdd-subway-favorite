package nextstep.auth.domain;

public class UserDetail {
    private String email;
    private String password;
    private Integer age;

    protected UserDetail() {}

    public UserDetail(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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

    public Boolean matchesPassword(String password) {
        return this.password.equals(password);
    }
}
