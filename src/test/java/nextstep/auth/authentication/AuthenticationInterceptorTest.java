package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.authFixture.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@DisplayName("인증 인터셉터(AuthenticationInterceptor)")
class AuthenticationInterceptorTest {

    @DisplayName("인증 이후 작업을 진행한다.")
    @Test
    void afterAuthentication() throws IOException {
        // given
        final AuthenticationInterceptor interceptor = mock(AuthenticationInterceptor.class);
        final Authentication authentication = mock(Authentication.class);
        final MockHttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        doNothing().when(interceptor).afterAuthentication(request, response, authentication);

        // when
        interceptor.afterAuthentication(request, response, authentication);

        // then, void then은 어떻게 처리하는게 좋을까요?
    }
}
