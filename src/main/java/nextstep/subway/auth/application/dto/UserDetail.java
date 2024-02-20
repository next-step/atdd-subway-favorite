package nextstep.subway.auth.application.dto;

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
}
