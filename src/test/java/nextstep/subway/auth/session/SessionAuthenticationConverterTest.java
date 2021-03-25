package nextstep.subway.auth.session;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.common.AuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.auth.session.SessionFixture.*;
import static nextstep.subway.auth.session.SessionFixture.createMockRequest;
import static org.assertj.core.api.Assertions.assertThat;

class SessionAuthenticationConverterTest {
    private AuthenticationConverter converter = new SessionAuthenticationConverter();

    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }
}
