package nextstep.auth.authentication.filter.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SimpleUser;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationProcessingFilterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private TokenAuthenticationProcessingFilter filter;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        filter = new TokenAuthenticationProcessingFilter(userDetailsService, jwtTokenProvider);
    }

    @Test
    void convert() throws IOException {
        var authenticationToken = filter.convert(createMockRequest());

        assertAll(
                () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
        );
    }

    @Test
    void authenticate() {
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(new SimpleUser(EMAIL, PASSWORD, List.of("ROLE_ADMIN")));

        var authentication = filter.authenticate(new AuthenticationToken(EMAIL, PASSWORD));

        assertAll(
                () -> assertThat(authentication.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(authentication.getAuthorities()).containsExactly("ROLE_ADMIN")
        );

    }

    @Test
    void processing() throws Exception {
        var roles = List.of("ROLE_ADMIN");
        var response = new MockHttpServletResponse();
        when(jwtTokenProvider.createToken(EMAIL, roles)).thenReturn(JWT_TOKEN);

        filter.processing(new Authentication(EMAIL, List.of("ROLE_ADMIN")), response);

        var expectedResponse = new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN));
        System.out.println(expectedResponse);
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}