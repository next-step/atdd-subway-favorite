package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("토큰 인증 기능 테스트")
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password1234";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TokenAuthenticationInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @DisplayName("BASIC 인증 스킴이 없을시 RuntimeException")
    @Test
    void convert_withoutHeader() {
       assertThatThrownBy(() -> interceptor.convert(request))
               .isInstanceOf(RuntimeException.class);
    }


    @DisplayName("HttpServletRequest에서 로그인 정보 추출하여 검증 객체 AuthenticationToken 생성")
    @Test
    void convert() {
        // given
        addAuthorizationHeader(request, EMAIL, PASSWORD);

        // when
        AuthenticationToken token = interceptor.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private void addAuthorizationHeader(MockHttpServletRequest request, String email, String password) {
        byte[] bytes = (email + ":" + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        String authorization = new String(encodedBytes);
        request.addHeader("Authorization", "BASIC " + authorization);
    }
}
