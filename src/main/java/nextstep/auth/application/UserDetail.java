package nextstep.auth.application;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetail {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public UserDetail() {
    }

    @Builder
    public UserDetail(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }
}
