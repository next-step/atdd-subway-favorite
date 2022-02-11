package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.unit.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class SessionAuthenticationConverterTest {
    AuthenticationConverter converter = new SessionAuthenticationConverter();

    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest request = createSessionMockRequest();

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }
}