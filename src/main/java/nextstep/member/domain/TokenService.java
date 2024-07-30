package nextstep.member.domain;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;

public interface TokenService {
    TokenResponse createToken(String email, String password);

    TokenResponse createTokenFromGithub(String code);
}
