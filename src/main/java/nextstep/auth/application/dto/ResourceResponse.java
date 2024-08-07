package nextstep.auth.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceResponse {

    private String email;
    private Integer age;

    public ResourceResponse(String email, Integer age) {
        this.email = email;
        this.age = age;
    }
}
