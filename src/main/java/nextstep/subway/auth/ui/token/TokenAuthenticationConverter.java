package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.ui.AuthenticationConverter;

public class TokenAuthenticationConverter implements AuthenticationConverter {

  private ObjectMapper mapper;

  public TokenAuthenticationConverter(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public AuthenticationToken convert(HttpServletRequest request){
    try {
      TokenRequest tokenRequest = mapper.readValue(request.getInputStream(),TokenRequest.class);
      String principal = tokenRequest.getEmail();
      String credentials = tokenRequest.getPassword();
      return new AuthenticationToken(principal, credentials);
    }
    catch (IOException e){
      throw new RuntimeException("토큰 생성에 실패하였습니다.");
    }
  }
}
