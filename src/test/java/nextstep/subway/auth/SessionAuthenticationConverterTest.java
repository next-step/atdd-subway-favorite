package nextstep.subway.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

@DisplayName("세션인증 컨버터 테스트")
public class SessionAuthenticationConverterTest {

  @DisplayName("HttpServletRequest에서 Form 정보로 AuthenticateionToken을 생성한다")
  @Test
  void convert() throws IOException, JSONException {
    // given
    AuthenticationConverter sessionAuthenticationConverter = new SessionAuthenticationConverter();
    MockHttpServletRequest request = AuthHelper.createMockRequest();
    // when
    AuthenticationToken authenticationToken = sessionAuthenticationConverter.convert(request);
    // then
    assertThat(authenticationToken.getPrincipal()).isEqualTo(AuthHelper.EMAIL);
    assertThat(authenticationToken.getCredentials()).isEqualTo(AuthHelper.PASSWORD);
  }

}
