package nextstep.auth.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("세션 기반 AuthenticationToken ")
class SessionAuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private SessionAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new SessionAuthenticationConverter();
    }

    @DisplayName("세션을 이용하여 AuthenticationToken을 만든다")
    @Test
    void convert() {
        // given
        converter = new SessionAuthenticationConverter();

        // when
        AuthenticationToken token = converter.convert(mock_요청생성());

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest mock_요청생성() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", EMAIL);
        request.addParameter("password", PASSWORD);

        return request;
    }


}