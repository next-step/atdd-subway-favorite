package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.SessionConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.user.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.subway.unit.AuthTarget.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SessionAuthenticationInterceptorTest {

  @Mock
  private UserDetailsService userDetailsService;

  @Mock
  private AuthenticationConverter authenticationConverter;

  private SessionAuthenticationInterceptor sessionAuthenticationInterceptor;

  @BeforeEach
  void setup() {
    authenticationConverter = new SessionConverter();
    sessionAuthenticationInterceptor = new SessionAuthenticationInterceptor(userDetailsService, authenticationConverter);
  }

  @Test
  void authenticate() {
    // given
    createMockLoginMember(userDetailsService);
    AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

    // when
    Authentication authentication = sessionAuthenticationInterceptor.authenticate(authenticationToken);

    // then
    assertThat(authentication.getPrincipal()).isEqualTo(AuthTarget.LOGIN_MEMBER);
  }

  @Test
  void preHandle() throws IOException {
    // given
    createMockLoginMember(userDetailsService);
    MockHttpServletRequest mockRequest = createSessionMockRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    // when
    sessionAuthenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());

    // then
    assertThat(mockResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
  }

}