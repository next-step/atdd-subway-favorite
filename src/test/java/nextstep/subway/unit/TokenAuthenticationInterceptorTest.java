package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @BeforeEach
    void setup(){
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 25));
    }

    @Test
    void convert() throws IOException {
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createMockRequest());

        assertAll(
          () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
          () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
        );
    }

    @Test
    void authenticate() throws IOException {
        // given
        Authentication targetAuth = new Authentication(customUserDetailsService.loadUserByUsername(EMAIL));
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createMockRequest());

        // when
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        assertThat(authentication.getPrincipal()).isEqualTo(targetAuth.getPrincipal());
    }

    @Test
    void invalidTokenAuthenticationTest() throws IOException {
        // given
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createInvalidMockRequest());

        // when & then
        assertThatThrownBy(() -> tokenAuthenticationInterceptor.authenticate(authenticationToken)).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest mockRequest = createMockRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        // when
        boolean result = tokenAuthenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());

        // then
        assertAll(
          () -> assertThat(result).isFalse(),
          () -> assertThat(mockResponse.getStatus()).isEqualTo(SC_OK),
          () -> assertThat(mockResponse.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)))
        );
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private MockHttpServletRequest createInvalidMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, "teststeststestst");
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
