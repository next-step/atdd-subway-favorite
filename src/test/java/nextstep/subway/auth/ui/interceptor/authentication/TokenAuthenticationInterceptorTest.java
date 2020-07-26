package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.AuthenticationProvider;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("토큰 기반 인증하는 인터셉터 테스트")
class TokenAuthenticationInterceptorTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String EMAIL = "dhlee@miridih.com";
    public static final String PASSWORD = "1234";

    public static final String TEMP_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwfSIsImlhdCI6MTU5NDcxODg5MywiZXhwIjoxNTk0NzIyNDkzfQ.SMyb9RNrs5Uy5eqVZ0jZw3SEgWFsZaifnlslI-cEQ-c";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private TokenAuthenticationInterceptor interceptor;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationProvider authenticationProvider;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        String credentials = new String(Base64.encodeBase64((EMAIL + ":" + PASSWORD).getBytes()));
        request.addHeader("Authorization", "Basic " + credentials);

        response = new MockHttpServletResponse();

        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 2);

        jwtTokenProvider = mock(JwtTokenProvider.class);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(TEMP_TOKEN);
        authenticationProvider = mock(AuthenticationProvider.class);
        when(authenticationProvider.authenticate(any())).thenReturn(new Authentication(loginMember));

        interceptor = new TokenAuthenticationInterceptor(authenticationProvider, jwtTokenProvider);
    }

    @Test
    @DisplayName("basic auth 요청을 interceptor에서 정상적으로 처리 되는지 테스트")
    public void preHandleTest() throws Exception {

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        // 응답 코드가 200
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        //
        TokenResponse tokenResponse = OBJECT_MAPPER.readValue(response.getContentAsString(), TokenResponse.class);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }
}