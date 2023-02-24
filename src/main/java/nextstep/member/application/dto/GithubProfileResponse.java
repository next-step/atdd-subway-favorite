package nextstep.member.application.dto;

import lombok.Getter;

@Getter
public class GithubProfileResponse {
    private String email;

    public GithubProfileResponse(final String email) {
        this.email = email;
    }
}
