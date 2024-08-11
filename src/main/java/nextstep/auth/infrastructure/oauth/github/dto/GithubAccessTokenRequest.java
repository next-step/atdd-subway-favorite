package nextstep.auth.infrastructure.oauth.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GithubAccessTokenRequest {

    private String code;
    private String clientId;
    private String clientSecret;
}
