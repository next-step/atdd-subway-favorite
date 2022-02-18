package nextstep.auth.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.unit.AuthData.EMAIL;
import static nextstep.auth.unit.AuthData.PASSWORD;
import static nextstep.auth.unit.MockRequest.OBJECT_MAPPER;
import static nextstep.auth.unit.MockRequest.createMockRequestWithToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorMockTest {
    private static final Long ID = 1L;
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthenticationConverter authenticationConverter;

    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @BeforeEach
    void setUp() {
        authenticationConverter = new TokenAuthenticationConverter(OBJECT_MAPPER);
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(
                userDetailsService, jwtTokenProvider, authenticationConverter, OBJECT_MAPPER);
    }

    @Test
    void preHandle() throws IOException {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new FakeLoginMember());
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        HttpServletRequest request = createMockRequestWithToken();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        tokenAuthenticationInterceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(OBJECT_MAPPER.writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    static class FakeLoginMember implements UserDetails {
        @Override
        public boolean checkPassword(String password) {
            return PASSWORD.equals(password);
        }

        @Override
        public Long getId() {
            return ID;
        }

        @Override
        public String getEmail() {
            return EMAIL;
        }
    }
}
