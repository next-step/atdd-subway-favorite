package nextstep.auth.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
class SessionAuthenticationInterceptorTest extends AuthTest {
    @Autowired
    private AuthenticationConverter sessionAuthenticationConverter;

    private SessionAuthenticationInterceptor sessionAuthenticationInterceptor;

    @Test
    void preHandle() throws IOException {
        // given
        sessionAuthenticationInterceptor = new SessionAuthenticationInterceptor(userDetailsService, sessionAuthenticationConverter);
        HttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        sessionAuthenticationInterceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);

        return request;
    }
}