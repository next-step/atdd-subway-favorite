package nextstep.api.auth.domain.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.application.dto.TokenResponse;
import nextstep.api.auth.domain.operators.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(String email) {
        return new TokenResponse(jwtTokenProvider.createToken(email));
    }
}
