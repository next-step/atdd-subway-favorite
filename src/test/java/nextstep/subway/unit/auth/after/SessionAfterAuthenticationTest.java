package nextstep.subway.unit.auth.after;

import static nextstep.auth.context.SecurityContextHolder.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.after.SessionAfterAuthentication;
import nextstep.auth.context.Authentication;

public class SessionAfterAuthenticationTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private SessionAfterAuthentication sessionAfterAuthentication;

    @BeforeEach
    void setUp() {
        this.sessionAfterAuthentication = new SessionAfterAuthentication();
    }

    @Test
    void convert() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = new Authentication(new AuthenticationToken(EMAIL, PASSWORD));

        // then
        sessionAfterAuthentication.afterAuthentication(request, response, authentication);

        // when
        //noinspection ConstantConditions
        assertThat(request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }
}
