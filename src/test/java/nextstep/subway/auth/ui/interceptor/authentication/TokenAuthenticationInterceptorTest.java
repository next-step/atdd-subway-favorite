package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.converter.TokenAuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "test@test.com";
    private static final String REGEX = ":";
    private static final String PASSWORD = "test";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private CustomUserDetailsService userDetailsService;
    private TokenAuthenticationInterceptor interceptor;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        setBasicAuthHeader();

        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
    }

    private void setBasicAuthHeader() {
        byte[] targetBytes = (EMAIL + REGEX + PASSWORD).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
    }

    @DisplayName("Basic Auth 인증")
    @Test
    void auth() {
        // given
        AuthenticationToken token = new TokenAuthenticationConverter().convert(request);

        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 1);
        when(userDetailsService.loadUserByUsername(token.getPrincipal())).thenReturn(loginMember);

        // when
        Authentication authentication = interceptor.authenticate(token);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @DisplayName("TokenResponse 응답")
    @Test
    void returnTokenResponse() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 1);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn("jwtToken");

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().contains("jwtToken")).isTrue();
    }
}
