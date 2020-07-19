package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("basic auth 요청을 interceptor에서 정상적으로 처리 되는지 테스트")
    public void preHandleTest() throws Exception {
        // given
        String id = "dhlee";
        String password = "test";
        String credentials = new String(Base64.encodeBase64((id + ":" + password).getBytes()));
        request.addHeader("Authorization", "Basic " + credentials);

        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor();

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