package nextstep.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.member.acceptance.MemberSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthServiceTest {

  @Autowired
  AuthService authService;

  @DisplayName("이메일과 패스워드로 토큰을 생성한다.")
  @Test
  void createToken() {
    // given
    var email = "domodazzi@gmail.com";
    var age = 20;
    MemberSteps.회원_생성_요청(email, "password", age);

    // when
    var token = authService.createToken(email, "password");

    // then
    var response = MemberSteps.회원_정보_조회_요청(token.getAccessToken());
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.jsonPath().getString("email")).isEqualTo(email);
    assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
  }

  @DisplayName("깃헙 액세스 코드로 토큰을 생성한다.")
  @Test
  void createTokenFromGithub() {
    // given
    var code = "domodazzi";

    // when
    var token = authService.createTokenFromGithub(code);

    // then
    var response = MemberSteps.회원_정보_조회_요청(token.getAccessToken());
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }
}