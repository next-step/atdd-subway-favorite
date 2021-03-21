package nextstep.subway.auth.ui.token;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.AuthenticationConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.auth.AuthRequestSteps.createMockTokenRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Token Authentication Converter 단위 테스트")
class TokenAuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private AuthenticationConverter authenticationConverter;

    @Test
    @DisplayName("Converter 추상화")
    void convert() throws IOException {
        // given
        authenticationConverter = new TokenAuthenticationConverter();
        MockHttpServletRequest request = createMockTokenRequest();

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
