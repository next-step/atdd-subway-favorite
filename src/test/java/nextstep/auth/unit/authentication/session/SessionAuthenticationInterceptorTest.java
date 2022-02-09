package nextstep.auth.unit.authentication.session;

import nextstep.auth.authentication.*;
import nextstep.auth.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);

        UserDetails expectedUserDetails = new DefaultUserDetails(EMAIL, PASSWORD);
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(expectedUserDetails);

        ProviderManager providerManager = new ProviderManager(Collections.singletonList(new UsernamePasswordAuthenticationProvider(userDetailsService)));
        interceptor = new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), providerManager);
    }

    @Test
    void preHandle() throws IOException {
        HttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, null);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String> params = new HashMap<>();
        params.put(SessionAuthenticationConverter.USERNAME_FIELD, EMAIL);
        params.put(SessionAuthenticationConverter.PASSWORD_FIELD, PASSWORD);
        request.addParameters(params);
        return request;
    }

}
