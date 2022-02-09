package nextstep.auth.authentication;

import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.context.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.util.authFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("인증 인터셉터(AuthenticationInterceptor)")
@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private UserDetailsService userDetailsService;

    @DisplayName("정상적인 요청, 응답, 인증 정보가 들어오면 인증 이후의 작업이 성공한다.")
    @Test
    void afterAuthentication() throws IOException {
        // given
        final AuthenticationInterceptor interceptor = createAuthenticationInterceptor(userDetailsService);
        final MockHttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final UserDetails userDetails = createUserDetails(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_AGE);
        final Authentication authentication = new Authentication(userDetails);

        interceptor.afterAuthentication(request, response, authentication);
    }

    @DisplayName("정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 있다.")
    @Test
    void authenticate() {
        // given
        final AuthenticationToken authenticationToken = new AuthenticationToken(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final AuthenticationInterceptor authenticationInterceptor = createAuthenticationInterceptor(userDetailsService);

        final UserDetails userDetails = createUserDetails(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_AGE);
        given(userDetailsService.loadUserByUsername(DEFAULT_EMAIL)).willReturn(userDetails);

        // when
        final Authentication authenticate = authenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(authenticate.getPrincipal()).isEqualTo(userDetails);
    }

    @DisplayName("비정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 없다")
    @Test
    void authenticateByNullUserDetails() throws IOException {
        // given
        final AuthenticationToken authenticationToken = new AuthenticationToken(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        final AuthenticationInterceptor authenticationInterceptor = createAuthenticationInterceptor(userDetailsService);
        given(userDetailsService.loadUserByUsername(DEFAULT_EMAIL)).willReturn(null);

        // when and then
        assertThatThrownBy(() -> authenticationInterceptor.authenticate(authenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("비정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 없다")
    @Test
    void authenticateByOtherPassword() throws IOException {
        // given
        final AuthenticationToken authenticationToken = new AuthenticationToken(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final AuthenticationInterceptor authenticationInterceptor = createAuthenticationInterceptor(userDetailsService);

        final UserDetails loginMember = createUserDetails(DEFAULT_ID, DEFAULT_EMAIL, "otherPassword", DEFAULT_AGE);
        given(userDetailsService.loadUserByUsername(DEFAULT_EMAIL)).willReturn(loginMember);

        // when
        assertThatThrownBy(() -> authenticationInterceptor.authenticate(authenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }
}
