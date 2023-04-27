package nextstep.github.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubAccessTokenResponse {
    private final String accessToken;
}
