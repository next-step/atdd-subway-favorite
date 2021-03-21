package nextstep.subway.auth;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.AuthenticationConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.auth.AuthRequestSteps.createMockRequest;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SessionAuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private AuthenticationConverter authenticationConverter;

    @Test
    @DisplayName("Convert 추상화 테스트")
    void convertSessionAuthentication() throws IOException {
        // given
        authenticationConverter = new SessionAuthenticationConverter();
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
