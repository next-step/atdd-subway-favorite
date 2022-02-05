package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.dto.TokenRequest;
import nextstep.auth.dto.TokenResponse;
import nextstep.auth.exception.InvalidPasswordException;
import nextstep.auth.infrastructure.JwtTokenProvider;
import nextstep.auth.ui.authentication.AuthenticationInterceptor;
import nextstep.auth.ui.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.ui.authentication.session.SessionAuthenticationInterceptor;
import nextstep.auth.ui.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.ui.authentication.token.TokenAuthenticationInterceptor;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationInterceptorTest {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String ANOTHER_PASSWORD = "another_password";
    private static final int AGE = 20;
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    CustomUserDetailsService userDetailsService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    AuthenticationInterceptor interceptor;

    @Test
    void preHandleWithSession() throws Exception {
        // given
        MockHttpServletRequest request = createRequestForSession();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor = new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), userDetailsService);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
    }

    @Test
    void invalidPasswordWithSession() {
        // given
        MockHttpServletRequest request = createRequestForSession();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor = new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), userDetailsService);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, ANOTHER_PASSWORD, AGE));

        // when, then
        assertThatThrownBy(() ->
                interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void preHandleWithToken() throws Exception{
        // given
        MockHttpServletRequest request = createRequestForToken();
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        interceptor = new TokenAuthenticationInterceptor(new TokenAuthenticationConverter(objectMapper),
                userDetailsService, jwtTokenProvider, objectMapper);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    @Test
    void invalidPasswordWithToken() throws IOException{
        // given
        MockHttpServletRequest request = createRequestForToken();
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, ANOTHER_PASSWORD, AGE));

        interceptor = new TokenAuthenticationInterceptor(new TokenAuthenticationConverter(objectMapper),
                userDetailsService, jwtTokenProvider, objectMapper);

        // when, then
        assertThatThrownBy(() ->
                interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(InvalidPasswordException.class);
    }

    private MockHttpServletRequest createRequestForSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }

    private MockHttpServletRequest createRequestForToken() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(objectMapper.writeValueAsString(new TokenRequest(EMAIL, PASSWORD)).getBytes());
        return request;
    }

}
