package nextstep.member.domain.command;

import nextstep.member.domain.entity.TokenPrincipal;

public interface TokenGenerator {
    String createToken(TokenPrincipal principal);
}
