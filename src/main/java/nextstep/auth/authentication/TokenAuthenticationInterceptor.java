package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.UserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;

  public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
    super(userDetailsService);
    this.jwtTokenProvider = jwtTokenProvider;
    this.objectMapper = objectMapper;
  }

  public AuthenticationToken convert(HttpServletRequest request) throws IOException {
    TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
    String principal = tokenRequest.getEmail();
    String credentials = tokenRequest.getPassword();

    return new AuthenticationToken(principal, credentials);
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
