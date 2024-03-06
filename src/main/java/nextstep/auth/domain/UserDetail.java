package nextstep.auth.domain;

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

    public UserDetail(String email) {
        this.email = email;
    }

    @Builder
    public UserDetail(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }
}
