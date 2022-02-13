package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nextstep.auth.util.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@DisplayName("인증 컨텍스트 인터셉터")
class SecurityContextInterceptorTest {

    @DisplayName("정상적인 데이터의 경우에는 true 를 반환한다")
    @Test
    void preHandle() throws Exception {
        // given
        final SecurityContextInterceptor interceptor = spy(SecurityContextInterceptor.class);
        final HttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final HttpServletResponse response = new MockHttpServletResponse();
        final Authentication authentication = new Authentication(createUserDetails(DEFAULT_PASSWORD));

        given(interceptor.getSecurityContext(any())).willReturn(new SecurityContext(authentication));

        // when
        final boolean actual = interceptor.preHandle(request, response, new Object());

        assertThat(actual).isTrue();
    }
}
