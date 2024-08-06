package nextstep.auth.domain.command;

import nextstep.auth.domain.entity.TokenPrincipal;

public interface EmailPasswordVerifier {
    TokenPrincipal verify(String email, String password);
}
