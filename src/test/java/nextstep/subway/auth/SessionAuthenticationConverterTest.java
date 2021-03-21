package nextstep.subway.auth;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.subway.auth.AuthRequestSteps.createMockSessionRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Session Authentication Converter 단위 테스트")
public class SessionAuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private AuthenticationConverter authenticationConverter;

    @Test
    @DisplayName("Convert 추상화")
    void convertSessionAuthentication() {
        // given
        authenticationConverter = new SessionAuthenticationConverter();
        MockHttpServletRequest request = createMockSessionRequest();

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
