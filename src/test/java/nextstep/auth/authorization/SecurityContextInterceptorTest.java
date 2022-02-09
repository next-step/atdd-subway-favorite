package nextstep.auth.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("인증 컨텍스트 인터셉터")
class SecurityContextInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("정상적인 데이터의 경우에는 false 를 반환한다")
    @Test
    void preHandle() throws Exception {
        // given
        final SecurityContextInterceptor interceptor = mock(SecurityContextInterceptor.class);
        final Authentication authentication = mock(Authentication.class);
        final MockHttpServletRequest request = createMockRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        given(interceptor.preHandle(request, response, authentication)).willReturn(false);

        // when
        final boolean actual = interceptor.preHandle(request, response, authentication);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("비정상적인 데이터의 경우에는 false 를 반환한다")
    @Test
    void preHandleOnInValidData() throws Exception {
        // given
        final SecurityContextInterceptor interceptor = mock(SecurityContextInterceptor.class);
        final Authentication authentication = mock(Authentication.class);
        final MockHttpServletRequest request = createMockRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        given(interceptor.preHandle(request, response, authentication)).willReturn(true);

        // when
        final boolean actual = interceptor.preHandle(request, response, authentication);

        // then
        assertThat(actual).isTrue();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
