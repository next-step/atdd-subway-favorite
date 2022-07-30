package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.interceptor.TokenAuthenticationFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationFilterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final UserDetails ADMIN_USER = new LoginMember(EMAIL, PASSWORD, List.of(ROLE_ADMIN.name()));
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @BeforeEach
    void setUp() {
        this.tokenAuthenticationFilter = new TokenAuthenticationFilter(userDetailsService, jwtTokenProvider);
    }

    @DisplayName("토큰 변환 검증")
    @Test
    void convert() throws IOException {
        // when
        AuthenticationToken authenticationToken = tokenAuthenticationFilter.getAuthenticationToken(createMockRequest());

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("Authentication 생성")
    @Test
    void authenticate() throws IOException {
        // given

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(ADMIN_USER);

        AuthenticationToken authenticationToken = tokenAuthenticationFilter.getAuthenticationToken(createMockRequest());

        // when
        Authentication authentication = tokenAuthenticationFilter.getAuthentication(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getAuthorities()).containsExactly(ROLE_ADMIN.name());
    }

    @DisplayName("jwt 토큰 생성 인터셉터 검증")
    @Test
    void preHandle() throws IOException {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(ADMIN_USER);
        when(jwtTokenProvider.createToken(EMAIL, List.of(ROLE_ADMIN.name()))).thenReturn(JWT_TOKEN);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        tokenAuthenticationFilter.preHandle(createMockRequest(), response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentAsString()).isEqualTo(OBJECT_MAPPER.writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(OBJECT_MAPPER.writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
