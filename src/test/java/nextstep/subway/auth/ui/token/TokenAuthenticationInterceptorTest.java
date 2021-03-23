package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.auth.AuthRequestSteps.createMockTokenRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper;
    private TokenAuthenticationConverter authenticationConverter;
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        authenticationConverter = new TokenAuthenticationConverter(objectMapper);
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, authenticationConverter, jwtTokenProvider, objectMapper);
    }

    @Test
    void authenticate() throws IOException {
        // given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(loginMember);

        // when
        MockHttpServletRequest request = createMockTokenRequest();
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    void preHandle() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(loginMember);
        given(jwtTokenProvider.createToken(anyString())).willReturn(JWT_TOKEN);

        // when
        MockHttpServletRequest request = createMockTokenRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }
}
