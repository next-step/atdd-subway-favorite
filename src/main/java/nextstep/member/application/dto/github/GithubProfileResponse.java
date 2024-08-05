package nextstep.member.application.dto.github;

import lombok.Getter;

@Getter
public class GithubProfileResponse {

    private String email;
    private int id;

    public GithubProfileResponse(String email, int id) {
        this.email = email;
        this.id = id;
    }
}
