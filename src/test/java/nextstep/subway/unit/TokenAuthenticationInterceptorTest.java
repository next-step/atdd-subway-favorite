package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest extends AuthenticationInterceptorTestSupport {
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private TokenAuthenticationInterceptor interceptor;

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
    void preHandle() throws IOException {
        // given
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 1));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);
        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when then
        assertDoesNotThrow(() -> interceptor.preHandle(request, response, null));
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
