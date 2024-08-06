package nextstep.auth.domain.infrastructure.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtConfig {
    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtConfig(
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expire-length}") long validityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.validityInMilliseconds =validityInMilliseconds;
    }
}
