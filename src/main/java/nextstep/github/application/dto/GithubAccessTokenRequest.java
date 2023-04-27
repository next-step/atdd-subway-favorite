package nextstep.github.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubAccessTokenRequest {
    private final String code;
    private final String clientId;
    private final String ClientSecret;
}
