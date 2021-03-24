package nextstep.subway.auth;

import nextstep.subway.auth.ui.converter.AuthenticationConverter;
import nextstep.subway.auth.ui.converter.SessionAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.auth.AuthSteps.*;

public class SessionAuthenticationConverterTest {
    @Test
    void convert() throws IOException {
        AuthenticationConverter converter = new SessionAuthenticationConverter();
        MockHttpServletRequest request = createMockSessionRequest();

        checkResponse(converter, request);
    }
}
