package nextstep.subway.auth.ui.interceptor.convertor;

import nextstep.subway.auth.ui.interceptor.converter.AuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.converter.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.converter.TokenAuthenticationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationConverterTest {
    AuthenticationConverter sessionConverter;
    AuthenticationConverter tokenConverter;

    @BeforeEach
    void setUp() {
        sessionConverter = new SessionAuthenticationConverter();
        tokenConverter = new TokenAuthenticationConverter();
    }

    @Test
    void createConverter() {
        assertThat(sessionConverter).isNotNull();
        assertThat(tokenConverter).isNotNull();
    }
}
