package nextstep.auth.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.entity.TokenPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailPasswordAuthenticator {
    private final EmailPasswordVerifier emailPasswordVerifier;
    private final TokenGenerator tokenGenerator;

    public String authenticate(String email, String password) {
        TokenPrincipal tokenPrincipal = emailPasswordVerifier.verify(email, password);
        return tokenGenerator.createToken(tokenPrincipal);
    }
}
