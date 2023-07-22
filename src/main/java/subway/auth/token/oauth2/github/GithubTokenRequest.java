package subway.auth.token.oauth2.github;

import lombok.Getter;

@Getter
public class GithubTokenRequest {
    private String code;
}
