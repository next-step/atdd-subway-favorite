package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.*;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.CustomUserDetailsService;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.subway.unit.AuthenticationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("세션 기반 로그인")
class SessionAuthenticationInterceptor2Test {

    private CustomUserDetailsService userDetailsService;
    private AuthenticationConverter converter;
    private SessionAuthenticationInterceptor2 interceptor;


    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        converter = mock(SessionAuthenticationConverter.class);
        interceptor = new SessionAuthenticationInterceptor2(userDetailsService, converter);
    }

    @Test
    void preHandle() throws Exception {
        //given
        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(userDetailsService.loadUserByUsername(any())).thenReturn(FIXTURE_LOGIN_MEMBER);
        when(converter.convert(any())).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));

        //when
        interceptor.preHandle(request, response, new Object());

        Object session = Objects.requireNonNull(request.getSession()).getAttribute(SPRING_SECURITY_CONTEXT_KEY);

        //then
        assertThat(session).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);

    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
