package nextstep.member.application;

import nextstep.member.application.dto.TokenResponse;

public interface TokenGenerator {
    TokenResponse generateToken(String email, String password);
}
