package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.SessionConverter;
import nextstep.auth.user.UserDetailsService;
import nextstep.subway.unit.authtarget.AuthTarget;
import nextstep.subway.unit.authtarget.InvalidAuthTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.subway.unit.authtarget.AuthTarget.createMockLoginMember;
import static nextstep.subway.unit.authtarget.AuthTarget.createSessionMockRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

  @Test
  void invalidTargetHandle() throws IOException {
    // given
    createMockLoginMember(userDetailsService);
    MockHttpServletRequest mockRequest = InvalidAuthTarget.createSessionMockRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    // when
    assertThatThrownBy(
      () -> sessionAuthenticationInterceptor.preHandle(mockRequest, mockResponse, new Object())
    ).isInstanceOf(AuthenticationException.class);
  }


  @Test
  void nonExistMemberHandle() throws IOException {
    // given
    MockHttpServletRequest mockRequest = AuthTarget.createSessionMockRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    // when & then
    assertThatThrownBy(
      () -> sessionAuthenticationInterceptor.preHandle(mockRequest, mockResponse, new Object())
    ).isInstanceOf(AuthenticationException.class);
  }


}