package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("토큰 기반 인증 Interceptor 유닛 테스트")
@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "high.neoul@gmail.com";
    private static final String PASSWORD = "high.neoul@gmail.com";
    private static final String DELIMITER = ":";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    // target interceptor to be tested on this class.
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        final byte[] targetBytes = (EMAIL + DELIMITER + PASSWORD).getBytes();
        final byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        final String credentials = new String(encodedBytes);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials);
    }

    @DisplayName("인터셉터가 일을 똑바로 하는지 알아보자 :)")
    @Test
    void preHandle() throws Exception {

        // given
        final LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 1);
        final String dummyToken = "dummy";

        // stubbing
        when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(any(String.class))).thenReturn(dummyToken);

        // when
        interceptor.preHandle(request, response, null);

        // then
        final String body = response.getContentAsString(StandardCharsets.UTF_8);
        assertThat(body).contains("accessToken");
        assertThat(body).contains(dummyToken);
    }
}