package nextstep.auth.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.session.SessionAuthenticationConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.auth.unit.AuthData.*;
import static org.assertj.core.api.Assertions.assertThat;

class SessionAuthenticationConverterTest {
    @DisplayName("form 타입의 요청을 AuthenticaionToken으로 변환한다")
    @Test
    void convert() throws IOException {
        // given
        AuthenticationConverter authenticationConverter = new SessionAuthenticationConverter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
