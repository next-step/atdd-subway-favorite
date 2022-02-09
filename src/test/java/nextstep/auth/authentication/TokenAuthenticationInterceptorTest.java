package nextstep.auth.authentication;

import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.util.authFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    @Mock
    private UserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;


    @DisplayName("정상적인 요청값이 들어왔을 경우 인증 토큰을 받을수 있다")
    @Test
    void convert() throws IOException {
        // when
        final MockHttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(request);

        // then
        assertAll(
                () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(DEFAULT_EMAIL),
                () -> assertThat(authenticationToken.getCredentials()).isEqualTo(DEFAULT_PASSWORD)
        );
    }

    @DisplayName("정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 있다")
    @Test
    void authenticate() throws IOException {
        // given
        final MockHttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(request);
        final UserDetails loginMember = createUserDetails(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_AGE);
        given(customUserDetailsService.loadUserByUsername(DEFAULT_EMAIL)).willReturn(loginMember);

        // when
        final Authentication authenticate = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(authenticate.getPrincipal()).isNotNull();
    }

    @DisplayName("비정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 없다")
    @Test
    void authenticateByNullUserDetails() throws IOException {
        // given
        final MockHttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(request);
        given(customUserDetailsService.loadUserByUsername(DEFAULT_EMAIL)).willReturn(null);

        // when
        assertThatThrownBy(() -> tokenAuthenticationInterceptor.authenticate(authenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("비정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 없다")
    @Test
    void authenticateByOtherPassword() throws IOException {
        // given
        final MockHttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(request);
        final UserDetails loginMember = createUserDetails(DEFAULT_ID, DEFAULT_EMAIL, "otherPassword", DEFAULT_AGE);
        given(customUserDetailsService.loadUserByUsername(DEFAULT_EMAIL)).willReturn(loginMember);

        // when
        assertThatThrownBy(() -> tokenAuthenticationInterceptor.authenticate(authenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("정상적인 요청값이 들어왔을 경우 다음 작업을 진행하지 않는다")
    @Test
    void preHandle() throws IOException {
        // given
        final MockHttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final UserDetails userDetails = createUserDetails(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_AGE);

        given(customUserDetailsService.loadUserByUsername(DEFAULT_EMAIL)).willReturn(userDetails);
        given(jwtTokenProvider.createToken(anyString())).willReturn(JWT_TOKEN);

        // when
        final boolean actual = tokenAuthenticationInterceptor.preHandle(request, response, new Object());

        // then
        assertThat(actual).isFalse();
    }
}
