package nextstep.auth.application;

import feign.Response;
import feign.codec.ErrorDecoder;
import nextstep.auth.exception.AuthenticationException;

public class GithubErrorDecoder implements ErrorDecoder {
  @Override
  public Exception decode(String methodKey, Response response) {
    return new AuthenticationException();
  }
}
