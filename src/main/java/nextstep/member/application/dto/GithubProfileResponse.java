package nextstep.member.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GithubProfileResponse {
    private String email;

    private int age;

    private String password;

    public GithubProfileResponse() {
    }

    @Builder
    public GithubProfileResponse(String email, int age, String password) {
        this.email = email;
        this.age = age;
        this.password = password;
    }
}
