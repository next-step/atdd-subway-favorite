package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.UserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;

  public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter authenticationConverter, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
    super(userDetailsService, authenticationConverter);
    this.jwtTokenProvider = jwtTokenProvider;
    this.objectMapper = objectMapper;
  }

  @Override
  public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
    String token = jwtTokenProvider.createToken(payload);
    TokenResponse tokenResponse = new TokenResponse(token);
    String responseToClient = objectMapper.writeValueAsString(tokenResponse);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getOutputStream().print(responseToClient);
  }

}
