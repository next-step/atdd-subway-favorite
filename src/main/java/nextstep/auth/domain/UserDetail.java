package nextstep.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetail {

  private String email;
  private String password;
  private Integer age;
}
