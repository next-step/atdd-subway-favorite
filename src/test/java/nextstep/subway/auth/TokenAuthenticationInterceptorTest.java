package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.NotFoundMemberException;
import nextstep.subway.auth.exception.NotMatchedPasswordException;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationInterceptor;
import nextstep.subway.auth.ui.token.TokenAuthenticator;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper = new ObjectMapper();

    private AuthenticationInterceptor authenticationInterceptor;

    @BeforeEach
    public void setup(){
        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        authenticationInterceptor = new AuthenticationInterceptor(objectMapper, userDetailsService, new TokenAuthenticator(jwtTokenProvider, objectMapper));
    }

    @Test
    void convert() throws Exception {
        //Given
        MockHttpServletRequest request = createMockRequest();

        //When
        AuthenticationToken authenticationToken = authenticationInterceptor.convert(request);

        //Then
        assertAll(
                () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
        );
    }

    @Test
    void authenticate() {
        //Given
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);

        //When
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 15));
        Authentication authentication = authenticationInterceptor.authenticate(token);

        //Then
        assertThat(authentication.getPrincipal()).isNotNull();
    }


    @Test
    void preHandle() throws Exception {
        //Given
        MockHttpServletResponse response = new MockHttpServletResponse();

        //When
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 15));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);
        boolean expected = authenticationInterceptor.preHandle(createMockRequest(), response, this);

        //Then
        assertAll(
                () -> assertThat(expected).isFalse(),
                () -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE),
                () -> assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)))

        );
    }

    @Test
    void preHandleNotMatchedPassword() {
        assertThatThrownBy(() -> {
            when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD + "1", 30));
            authenticationInterceptor.preHandle(createMockRequest(), new MockHttpServletResponse(), this);
        }).isInstanceOf(NotMatchedPasswordException.class);
    }

    @Test
    void preHandleNotFoundMember(){
        assertThatThrownBy(() -> {
            when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(null);
            authenticationInterceptor.preHandle(createMockRequest(), new MockHttpServletResponse(), this);
        }).isInstanceOf(NotFoundMemberException.class);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
