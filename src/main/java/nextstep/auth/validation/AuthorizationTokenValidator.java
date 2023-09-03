package nextstep.auth.validation;

import lombok.RequiredArgsConstructor;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthorizationTokenValidator extends AuthorizationValidator {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void validate(String authorization) {
        String token = authorization.split(" ")[1];

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }
    }
}
