package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationToken;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class UsernamePasswordAuthenticationFilterTest {

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";

    private AuthenticationRespondingFilter authFilter = new UsernamePasswordAuthenticationFilter(null);

    @Test
    void convert() throws IOException {
        // given
        HttpServletRequest request = formAuthHttpRequest(EMAIL, PASSWORD);

        // when
        AuthenticationToken token = authFilter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest formAuthHttpRequest(String email, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", email);
        request.setParameter("password", password);
        return request;
    }
}
