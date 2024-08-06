package nextstep.auth.domain.command;

import nextstep.auth.domain.entity.TokenPrincipal;

public interface TokenGenerator {
    String createToken(TokenPrincipal principal);
}
