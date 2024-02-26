package nextstep.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceTest {

  @Autowired
  AuthService authService;

  @DisplayName("이메일과 패스워드로 토큰을 생성한다.")
  @Test
  void createToken() {
  }

  @DisplayName("깃헙 액세스 코드로 토큰을 생성한다.")
  @Test
  void createTokenFromGithub() {
  }
}