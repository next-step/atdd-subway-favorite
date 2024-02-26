package nextstep.auth.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubProfileResponse {

  private String email;

  private int age;
}
