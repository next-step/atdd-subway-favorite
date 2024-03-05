package nextstep.auth.oauth.github;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse(String email) {
        this.email = email;
    }

}
