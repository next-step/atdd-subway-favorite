package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.member.application.UserDetailsService;
import nextstep.member.domain.LoginMember;
import org.assertj.core.api.AssertionsForClassTypes;
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
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class SessionAuthenticationInterceptorTest {

  @Mock
  private UserDetailsService userDetailsService;

  private SessionAuthenticationInterceptor sessionAuthenticationInterceptor;

  @BeforeEach
  void setup() {
    sessionAuthenticationInterceptor = new SessionAuthenticationInterceptor(userDetailsService);
  }

  @Test
  void authenticate() {
    // given
    createMockLoginMember(userDetailsService);
    AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
    LoginMember loginMember = AuthTarget.LOGIN_MEMBER;

    // when
    Authentication authentication = sessionAuthenticationInterceptor.authenticate(authenticationToken);

    // then
    assertThat(authentication.getPrincipal()).isEqualTo(loginMember);
  }

  @Test
  void preHandle() throws IOException {
    // given
    MockHttpServletRequest mockRequest = createSessionMockRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    createMockLoginMember(userDetailsService);

    // when
    sessionAuthenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());

    // then
    assertThat(mockResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
  }


  @Test
  void convert() throws IOException {
    // when
    AuthenticationToken authenticationToken = sessionAuthenticationInterceptor.convert(createSessionMockRequest());

    // then
    assertAll(
      () -> AssertionsForClassTypes.assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
      () -> AssertionsForClassTypes.assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
    );
  }


}