package nextstep.subway.auth;

import nextstep.subway.auth.ui.converter.AuthenticationConverter;
import nextstep.subway.auth.ui.converter.TokenAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.auth.AuthSteps.*;

public class TokenAuthenticationConverterTest {
    @Test
    void convert() throws IOException {
        AuthenticationConverter converter = new TokenAuthenticationConverter();
        MockHttpServletRequest request = createMockRequest();

        checkResponse(converter, request);
    }
}
