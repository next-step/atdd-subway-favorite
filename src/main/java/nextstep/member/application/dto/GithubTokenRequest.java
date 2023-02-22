package nextstep.member.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubTokenRequest {
    private final String code;
    private final String clientId;
    private final String clientSecret;

}
