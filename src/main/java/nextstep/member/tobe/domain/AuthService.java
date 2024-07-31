package nextstep.member.tobe.domain;

import nextstep.member.tobe.application.dto.TokenRequest;
import nextstep.member.tobe.application.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(TokenRequest request);
}

