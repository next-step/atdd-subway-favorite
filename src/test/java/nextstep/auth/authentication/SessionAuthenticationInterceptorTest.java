package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.authentication.step.AuthenticationStep.session_인증_요청_mock;
import static nextstep.auth.authentication.step.AuthenticationStep.인증_응답_mock;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class})
class SessionAuthenticationInterceptorTest {

    private CustomUserDetailsService userDetailsService;
    private AuthenticationConverter authenticationConverter;
    private Authentication authentication;
    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, authenticationConverter);
    }

    @DisplayName("인증 정보를확인 하고 응답한다")
    @Test
    void afterAuthentication() throws IOException {
        // given
        MockHttpServletResponse 응답 = 인증_응답_mock();

        // when
        interceptor.afterAuthentication(session_인증_요청_mock(), 응답, authentication);

        // then
        assertThat(응답.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}