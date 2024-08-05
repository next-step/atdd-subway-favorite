package nextstep.member.application.dto.github;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubProfileResponse {

    private String email;
    private int age;

    public GithubProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }
}
