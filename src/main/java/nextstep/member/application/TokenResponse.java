package nextstep.member.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TokenResponse {
    private final String accessToken;
}
