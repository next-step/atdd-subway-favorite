package nextstep.member.domain;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(TokenRequest request);
}

