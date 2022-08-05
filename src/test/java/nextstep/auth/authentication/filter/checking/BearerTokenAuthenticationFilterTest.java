package nextstep.auth.authentication.filter.checking;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BearerTokenAuthenticationFilterTest {

    private static final String EMAIL = "email@email.com";
    private static final List<String> ROLES = List.of("ROLE_ADMIN");
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private BearerTokenAuthenticationFilter filter;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        filter = new BearerTokenAuthenticationFilter(jwtTokenProvider);
    }

    @Test
    void convert() throws IOException {
        var authenticationToken = filter.convert(createMockRequest());

        assertAll(
                () -> assertThat(authenticationToken.getPrincipal()).isNull(),
                () -> assertThat(authenticationToken.getCredentials()).isEqualTo(TOKEN)
        );
    }

    @Test
    void authenticate() {
        when(jwtTokenProvider.validateToken(TOKEN)).thenReturn(true);
        when(jwtTokenProvider.getPrincipal(TOKEN)).thenReturn(EMAIL);
        when(jwtTokenProvider.getRoles(TOKEN)).thenReturn(ROLES);

        var authentication = filter.authenticate(new AuthenticationToken(null, TOKEN));

        assertAll(
                () -> assertThat(authentication.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(authentication.getAuthorities()).containsAnyElementsOf(ROLES)
        );

    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("authorization", "Bearer " + TOKEN);
        return request;
    }

}