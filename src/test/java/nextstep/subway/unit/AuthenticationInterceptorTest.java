package nextstep.subway.unit;

import nextstep.auth.adapter.in.UserDetailsService;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.subway.unit.AuthTarget.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

  @Mock
  private UserDetailsService userDetailsService;

  private AuthenticationInterceptor authenticationInterceptor;

  @BeforeEach
  void setup() {
    authenticationInterceptor = new AuthenticationInterceptor(userDetailsService) {
      @Override
      public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return AUTH_TOKEN;
      }

      @Override
      public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
      }
    };
  }

  @Test
  void preHandle() throws IOException {
    // given
    createMockLoginMember(userDetailsService);
    MockHttpServletRequest mockRequest = createTokenMockRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    // when
    boolean result = authenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());

    // then
    assertThat(result).isFalse();
  }

}
