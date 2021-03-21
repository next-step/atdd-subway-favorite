package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AbstractAuthenticationInterceptor;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.http.MediaType;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

  private JwtTokenProvider jwtTokenProvider;
  private ObjectMapper objectMapper;

  public TokenAuthenticationInterceptor(CustomUserDetailsService userDetailsService,
      AuthenticationConverter authenticationConverter,
      JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
    super(userDetailsService, authenticationConverter);
    this.jwtTokenProvider = jwtTokenProvider;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    TokenResponse tokenResponse = createTokenResponse(authentication);
    setResponse(response, tokenResponse);
  }

  private TokenResponse createTokenResponse(Authentication authentication) throws IOException {
    String jwtPayLoad = objectMapper.writeValueAsString(authentication.getPrincipal());
    String accessToken = jwtTokenProvider.createToken(jwtPayLoad);
    return new TokenResponse(accessToken);
  }

  private void setResponse(HttpServletResponse response, TokenResponse tokenResponse)
      throws IOException {
    String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getOutputStream().print(responseToClient);
  }
}
