package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationToken;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class BasicAuthenticationFilterTest {

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";

    private final AuthenticationSavingFilter<AuthenticationToken> authFilter =
            new BasicAuthenticationFilter(null);

    @Test
    void convert() {
        // given
        HttpServletRequest request = basicAuthHttpRequest(EMAIL, PASSWORD);

        // when
        AuthenticationToken token = authFilter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest basicAuthHttpRequest(String email, String password) {
        String encodedBasicAuth = Base64.getEncoder()
                .encodeToString(String.format("%s:%s", email, password).getBytes());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic " + encodedBasicAuth);
        return request;
    }
}
