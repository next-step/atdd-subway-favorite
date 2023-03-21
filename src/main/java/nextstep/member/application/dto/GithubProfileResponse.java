package nextstep.member.application.dto;

import lombok.Getter;

@Getter
public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse(String email) {
        this.email = email;
    }

    public static GithubProfileResponse of(String principal) {
        return new GithubProfileResponse(principal);
    }
}
