package nextstep.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.auth.AuthenticationException;

@Getter
@AllArgsConstructor
public class UserDetail {

  private String email;
  private String password;
  private Integer age;

  public void checkPassword(final String password) {
    if (!this.password.equals(password)) {
      throw new AuthenticationException("인증 정보가 올바르지 않습니다.");
    }
  }
}
