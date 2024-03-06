package nextstep.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GithubProfileResponse {
    private String email;

    private int age;

    public GithubProfileResponse() {
    }

    @Builder
    public GithubProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }
}
