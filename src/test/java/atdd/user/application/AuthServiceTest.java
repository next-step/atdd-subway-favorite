package atdd.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import atdd.configure.JwtConfig;
import atdd.user.application.dto.AuthInfoView;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import static atdd.user.TestConstant.*;

@SpringBootTest
public class AuthServiceTest {
  @Autowired
  private JwtConfig jwtConfig;

  @Autowired
  private AuthService authService;

  private AuthTestUtil authTestUtil;

  @BeforeEach
  void setUp() {
    this.authTestUtil = new AuthTestUtil();
  }

  @Test
  void generateAuthToken() {
    AuthInfoView authInfoView = authService.GenerateAuthToken(USER_1_EMAIL);

    assertThat(authInfoView.getTokenType()).isNotNull();
    assertThat(authInfoView.getAccessToken()).isNotNull();

    Claims claims = authTestUtil.ParseAuthToken(jwtConfig, authInfoView.getAccessToken());
    assertThat(claims.getSubject()).isEqualTo(USER_1_EMAIL);
  }

  @Test
  void authUserToken() {
    AuthInfoView authInfoView = authService.GenerateAuthToken(USER_1_EMAIL);

    Optional<String> result = authService.AuthUser(authInfoView);
    if(!result.isPresent()) {
      fail("인증 실패");
      return;
    }

    assertThat(result.get()).isEqualTo(USER_1_EMAIL);
  }

}
