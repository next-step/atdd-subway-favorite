package nextstep.subway.auth.domain;

public class UserDetail {
    private String email;
    private String password;
    private Integer age;

    public UserDetail(String email,
                      String password,
                      Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public boolean correctPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
