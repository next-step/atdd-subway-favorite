package nextstep.auth.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GithubAccessTokenRequest {
    private String code;
    private String clientId;
    private String clientSecret;
}
