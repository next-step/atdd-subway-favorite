package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@DisplayName("인증 인터셉터(AuthenticationInterceptor)")
class AuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("인증 이후 작업을 진행한다.")
    @Test
    void afterAuthentication() throws IOException {
        // given
        final AuthenticationInterceptor interceptor = mock(AuthenticationInterceptor.class);
        final Authentication authentication = mock(Authentication.class);
        final MockHttpServletRequest request = createMockRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        doNothing().when(interceptor).afterAuthentication(request, response, authentication);

        // when
        interceptor.afterAuthentication(request, response, authentication);

        // then
        then(response).should(times(1)).setStatus(any());
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
