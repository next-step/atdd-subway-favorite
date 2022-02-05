package nextstep.auth.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.InvalidPasswordException;
import nextstep.auth.authentication.NotExistEmailException;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String WRONG_EMAIL = "wrongemail@email.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrongpassword";
    private static final int AGE = 20;
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    CustomUserDetailsService userDetailsService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Spy
    ObjectMapper objectMapper;

    @InjectMocks
    TokenAuthenticationInterceptor interceptor;

    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken authenticationToken = interceptor.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        // when
        Authentication authenticate = interceptor.authenticate(authenticationToken);

        // then
        assertThat(authenticate.getPrincipal()).isNotNull();
    }

    @Test
    void authenticateInvalidPassword() {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, WRONG_PASSWORD);

        // when, then
        assertThatThrownBy(() ->
                interceptor.authenticate(authenticationToken))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void preHandle() throws IOException {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
