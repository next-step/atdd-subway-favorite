package nextstep.subway.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

@DisplayName("세션인증 컨버터 테스트")
public class SessionAuthenticationConverterTest {

  private AuthenticaionConverter sessionAuthenticaionConverter;

  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";


  @DisplayName("HttpServletRequest에서 Form 정보로 AuthenticateionToken을 생성한다")
  @Test
  void convert() throws IOException, JSONException {
    // given
    sessionAuthenticaionConverter = new SessionAuthenticationConverter();
    MockHttpServletRequest request = createMockRequest();
    // when
    AuthenticationToken authenticationToken = sessionAuthenticaionConverter.convert(request);
    // then
    assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
    assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
  }

  private MockHttpServletRequest createMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter("username",EMAIL);
    request.addParameter("password",PASSWORD);
    return request;
  }

}
