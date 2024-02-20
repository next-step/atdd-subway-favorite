package nextstep.subway.auth.application.provider;

import nextstep.subway.auth.AuthenticationException;

import java.util.Arrays;
import java.util.Objects;

public enum TokenType {
    JWT("Bearer"),
    GITHUB("Github");

    private final String prefix;

    TokenType(String prefix) {
        this.prefix = prefix;
    }

    public static TokenType findBy(String prefix) {
        return Arrays.stream(TokenType.values())
                .filter(tokenType -> Objects.equals(tokenType.prefix, prefix))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("잘못된 토큰 타입입니다."));
    }

    public String getPrefix() {
        return prefix;
    }
}
