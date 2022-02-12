package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.util.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("인증 인터셉터(AuthenticationInterceptor)")
class AuthenticationInterceptorTest {

    @DisplayName("정상적인 요청, 응답, 인증 정보가 들어오면 인증 이후의 작업이 성공한다.")
    @Test
    void afterAuthentication() throws IOException {
        // given
        final AuthenticationInterceptor interceptor = mock(AuthenticationInterceptor.class);
        final HttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final HttpServletResponse response = new MockHttpServletResponse();
        final Authentication authentication = new Authentication(createUserDetails(DEFAULT_PASSWORD));

        doNothing().when(interceptor).afterAuthentication(request, response, authentication);

        // when and then
        interceptor.afterAuthentication(request, response, authentication);
    }

    @DisplayName("비정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 없다")
    @Test
    void authenticateByNullUserDetails() throws IOException {
        // given
        final AuthenticationInterceptor interceptor = mock(AuthenticationInterceptor.class);
        final HttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final HttpServletResponse response = new MockHttpServletResponse();
        final Authentication authentication = new Authentication(createUserDetails(DEFAULT_PASSWORD));

        doThrow(new NullPointerException()).when(interceptor).afterAuthentication(request, response, authentication);

        // when and then
        assertThatThrownBy(() -> interceptor.afterAuthentication(request, response, authentication))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("비정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 없다")
    @Test
    void authenticateByOtherPassword() throws IOException {
        // given
        final AuthenticationInterceptor interceptor = mock(AuthenticationInterceptor.class);
        final HttpServletRequest request = createMockRequest(DEFAULT_EMAIL, "otherPassword");
        final HttpServletResponse response = new MockHttpServletResponse();
        final Authentication authentication = new Authentication(createUserDetails(DEFAULT_PASSWORD));

        doThrow(new AuthenticationException()).when(interceptor).afterAuthentication(request, response, authentication);

        // when and then
        assertThatThrownBy(() -> interceptor.afterAuthentication(request, response, authentication))
                .isInstanceOf(AuthenticationException.class);
    }
}
