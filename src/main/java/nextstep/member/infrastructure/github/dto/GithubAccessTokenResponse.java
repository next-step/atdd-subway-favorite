package nextstep.member.infrastructure.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubAccessTokenResponse {
    String accessToken;
    String refreshToken;
    String tokenType;
}
