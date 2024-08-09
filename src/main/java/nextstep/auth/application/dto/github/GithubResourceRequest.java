package nextstep.auth.application.dto.github;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubResourceRequest {

    private String accessToken;

    public GithubResourceRequest(String accessToken) {
        this.accessToken = accessToken;
    }
}
