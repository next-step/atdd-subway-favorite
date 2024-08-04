package nextstep.member.infrastructure.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubAccessTokenRequest {
    String clientId;
    String clientSecret;
    String code;
}
