package nextstep.member.config;

import nextstep.common.exception.CannotInstanceException;

public final class BearerTokenExtractor {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private BearerTokenExtractor() {
        throw new CannotInstanceException();
    }

    public static String extract(String token) {
        return token.substring(BEARER_TOKEN_PREFIX.length());
    }
}
