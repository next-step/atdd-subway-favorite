package nextstep.subway.auth.acceptance.ui.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "test@test.com";
    private static final String REGEX = ":";
    private static final String PASSWORD = "test";
    MockHttpServletRequest request;
    private CustomUserDetailsService userDetailsService;


    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        setBasicAuthHeadner();

        userDetailsService = mock(CustomUserDetailsService.class);
    }

    private void setBasicAuthHeadner() {
        byte[] targetBytes = (EMAIL + REGEX + PASSWORD).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
    }

    @DisplayName("Basic Auth 로그인 정보 추출")
    @Test
    void extractAuth() {
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService);

        // when
        AuthenticationToken token = interceptor.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("Basic Auth 인증")
    @Test
    void auth() {
        // given
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService);
        AuthenticationToken token = interceptor.convert(request);

        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 1);
        when(userDetailsService.loadUserByUsername(token.getPrincipal())).thenReturn(loginMember);

        // when
        Authentication authentication = interceptor.authenticate(token);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

}
