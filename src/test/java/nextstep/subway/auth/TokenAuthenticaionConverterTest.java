package nextstep.subway.auth;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.ui.token.TokenAuthenticationConverter;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

@DisplayName("토큰기반 인증 테스트")
public class TokenAuthenticaionConverterTest {
  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";
  public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

  @DisplayName("HttpServletRequest에서 HTTP Request Body의 정보로 AuthenticateionToken을 생성한다")
  @Test
  void convert() throws IOException, JSONException {
    // given
    TokenAuthenticationConverter tokenAuthenticaionConverter = new TokenAuthenticationConverter(new ObjectMapper());
    MockHttpServletRequest request = createMockRequest();
    // when
    AuthenticationToken authenticationToken = tokenAuthenticaionConverter.convert(request);
    // then
    assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
    assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
  }

  private MockHttpServletRequest createMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
    request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
    return request;
  }
}
