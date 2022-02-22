package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenConverter implements AuthenticationConverter {
  private final ObjectMapper objectMapper;

  public TokenConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public AuthenticationToken convert(HttpServletRequest request) throws IOException {
    TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
    String principal = tokenRequest.getEmail();
    String credentials = tokenRequest.getPassword();

    LoggerFactory.getLogger(this.getClass()).info("Principal = {}, Credentials = {}", principal, credentials);
    return new AuthenticationToken(principal, credentials);
  }
}
