package nextstep.subway.unit.service;

import io.cucumber.java.an.E;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.subway.unit.TokenAuthenticationUnitTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.PASSWORD;
import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.createMockRequest;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    /**
     * Given 토큰 생성 요청 객체를 만들고
     * When HttpServletRequest 의 바디에 담아서 인증 토큰으로 변환을 요청하면
     * Then 변환된 인증 토큰을 받는다
     */
    @Test
    void convert() throws IOException {
        MockHttpServletRequest request = createMockRequest();
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(request);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() {
    }

    @Test
    void preHandle() throws IOException {
    }
}
