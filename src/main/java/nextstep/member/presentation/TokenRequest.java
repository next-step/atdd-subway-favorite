package nextstep.member.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TokenRequest {
    private final String email;
    private final String password;
}
