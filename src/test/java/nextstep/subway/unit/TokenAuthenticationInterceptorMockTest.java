package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.LoginMember;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.UserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TokenAuthenticationInterceptorMockTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    UserDetailsService userDetailsService;

    ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    TokenAuthenticationInterceptor interceptor;

    @Test
    void convert() throws IOException {
        AuthenticationToken token = interceptor.convert(createMockRequest());

        assertThat(token).isEqualTo(new AuthenticationToken(EMAIL, PASSWORD));
    }

    @Test
    void authenticate() throws IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(jwtTokenProvider.createToken(any(), any())).thenReturn(JWT_TOKEN);

        interceptor.authenticate(new LoginMember(EMAIL, PASSWORD, List.of()), response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(mapper.readValue(response.getContentAsString(), TokenResponse.class)).isEqualTo(new TokenResponse(JWT_TOKEN));
    }

    @Test
    void preHandle() throws IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        given(userDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(EMAIL, PASSWORD, List.of()));
        given(jwtTokenProvider.createToken(any(), any())).willReturn(JWT_TOKEN);
        boolean result = interceptor.preHandle(createMockRequest(), response, new Object());

        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(mapper.writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(mapper.writeValueAsString(tokenRequest).getBytes());

        return request;
    }
}
