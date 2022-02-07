package nextstep.subway.unit.auth.converter;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.auth.AuthConfig;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.AuthenticationConverters;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;

class AuthenticationConvertersTest {
    private AuthenticationConverters authenticationConverters;

    @BeforeEach
    void setUp() {
        authenticationConverters = new AuthenticationConverters(Arrays.asList(
            new SessionAuthenticationConverter(),
            new TokenAuthenticationConverter()
        ));
    }

    private static Stream<Arguments> uriAndClazz() {
        return Stream.of(
            Arguments.of(AuthConfig.SESSION_LOGIN_REQUEST_URI, SessionAuthenticationConverter.class),
            Arguments.of(AuthConfig.TOKEN_LOGIN_REQUEST_URI, TokenAuthenticationConverter.class)
        );
    }

    @DisplayName("특정 Request Uri에 Converter가 정확히 매칭 되는지 테스트")
    @MethodSource("uriAndClazz")
    @ParameterizedTest
    void converter(String requestUri, Class<?> clazz) {
        assertThat(authenticationConverters.authenticationConverter(requestUri).getClass()).isEqualTo(clazz);
    }
}
