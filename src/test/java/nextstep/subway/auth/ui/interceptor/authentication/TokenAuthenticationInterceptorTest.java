package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("토큰 기반 인증하는 인터셉터 테스트")
class TokenAuthenticationInterceptorTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String EMAIL = "dhlee@miridih.com";
    public static final String PASSWORD = "1234";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private TokenAuthenticationInterceptor interceptor;

    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        interceptor = new TokenAuthenticationInterceptor();
    }

    @Test
    @DisplayName("basic auth 요청을 interceptor에서 정상적으로 처리 되는지 테스트")
    public void preHandleTest() throws Exception {
        // given
        String credentials = new String(Base64.encodeBase64((EMAIL + ":" + PASSWORD).getBytes()));
        request.addHeader("Authorization", "Basic " + credentials);

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

    @Test
    @DisplayName("token stirng으로부터 AuthenticationToken으로 변환한다")
    void convertToken() {
        String token = Base64.encodeBase64String((EMAIL + ":" + PASSWORD).getBytes());
        AuthenticationToken authenticationToken = interceptor.convertToken(token);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}