package nextstep.member;

import feign.Response;
import feign.codec.ErrorDecoder;
import nextstep.member.exception.AuthenticationException;

public class GithubErrorDecoder implements ErrorDecoder {
  @Override
  public Exception decode(String methodKey, Response response) {
    return new AuthenticationException();
  }
}
