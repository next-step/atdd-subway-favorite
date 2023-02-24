package nextstep.member.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubTokenResponse {
    private final String code;
    private final String accessToken;
    private final String email;
}
