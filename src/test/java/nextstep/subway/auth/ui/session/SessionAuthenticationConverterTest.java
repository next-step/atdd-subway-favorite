package nextstep.subway.auth.ui.session;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionAuthenticationConverterTest {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    private static final String EMAIL = "gpwls89";
    private static final String PASSWORD = "1234";

    private SessionAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new SessionAuthenticationConverter();
    }

    @Test
    void convert() {
        //when
        AuthenticationToken authenticationToken = converter.convert(createMockRequest());

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(USERNAME_FIELD, EMAIL);
        paramMap.put(PASSWORD_FIELD, PASSWORD);
        request.setParameters(paramMap);
        return request;
    }
}
