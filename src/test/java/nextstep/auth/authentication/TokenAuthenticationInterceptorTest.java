package nextstep.auth.authentication;

import nextstep.auth.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.util.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenAuthenticationInterceptorTest {

    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        userDetailsService = createUserDetailsService(DEFAULT_PASSWORD);
        jwtTokenProvider = createJwtTokenProvider();
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
    }

    @DisplayName("정상적인 요청값이 들어왔을 경우 인증 토큰을 받을수 있다")
    @Test
    void convert() throws IOException {
        // given
        final HttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        // when
        final AuthenticationToken authenticationToken = interceptor.convert(request);

        // then
        assertThat(authenticationToken).isNotNull();
    }

    @DisplayName("정상적인 요청값이 들어왔을 경우 다음 작업을 진행하지 않는다")
    @Test
    void preHandle() throws IOException {
        // given
        final HttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final HttpServletResponse response = new MockHttpServletResponse();

        // when
        final boolean actual = interceptor.preHandle(request, response, new Object());

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("비정상적인 요청값이 들어왔을 경우 다음 작업을 진행하지 않는다")
    @Test
    void authenticateByNullUserDetails() throws IOException {
        // given
        final HttpServletRequest request = createMockRequest(null, DEFAULT_PASSWORD);
        final HttpServletResponse response = new MockHttpServletResponse();
        final Authentication authentication = new Authentication(createUserDetails(DEFAULT_PASSWORD));

        // when and then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, authentication))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("비정상적인 요청값이 들어왔을 경우 다음 작업을 진행하지 않는다")
    @Test
    void authenticateByOtherPassword() throws IOException {
        // given
        final HttpServletRequest request = createMockRequest(DEFAULT_EMAIL, "otherPassword");
        final HttpServletResponse response = new MockHttpServletResponse();
        final Authentication authentication = new Authentication(createUserDetails(DEFAULT_PASSWORD));

        // when and then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, authentication))
                .isInstanceOf(AuthenticationException.class);
    }
}
