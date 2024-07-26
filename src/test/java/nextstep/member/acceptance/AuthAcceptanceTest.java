package nextstep.member.acceptance;

import static nextstep.Fixtures.aMember;
import static nextstep.member.acceptance.steps.AuthAcceptanceSteps.*;
import static nextstep.member.acceptance.steps.MemberAcceptanceSteps.내_정보_조회_요청;
import static nextstep.member.acceptance.steps.MemberAcceptanceSteps.내_정보_조회됨;
import static nextstep.member.acceptance.support.GithubResponses.사용자;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.support.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("인증 인수 테스트")
class AuthAcceptanceTest extends AcceptanceTest {
  private Member member = aMember().build();

  @Autowired private MemberRepository memberRepository;

  /** Given 회원이 생성되어 있고 */
  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();
    member = memberRepository.save(member);
  }

  /** When 로그인 요청을 하면 Then 로그인이 성공해서 토큰이 반환되고 AND 토큰을 사용해서 내 정보를 조회할 수 있다. */
  @DisplayName("Bearer Auth")
  @Test
  void bearerAuth() {
    String accessToken = 로그인_요청(member.getEmail(), member.getPassword());
    assertThat(accessToken).isNotBlank();

    var response = 내_정보_조회_요청(accessToken);

    내_정보_조회됨(response, member);
  }

  /** When 로그인 요청을 하면 Then 로그인이 성공해서 토큰이 반환되고 AND 토큰을 사용해서 내 정보를 조회할 수 있다. */
  @DisplayName("잘못된 인증 정보")
  @ParameterizedTest
  @CsvSource(
      value = {
        ",",
        "admin@email.com,",
        ",password",
        "DOES_NOT_EXIST@email.com,password",
        "admin@email.com,bad password",
        "DOES_NOT_EXIST@email.com,bad password"
      })
  void invalidAuth(String email, String password) {
    var response = 잘못된_정보로_로그인_요청(email, password);

    인증_실패함(response);
  }

  /** When 위변조된 토큰을 사용해서 내 정보를 조회하면 Then 인증 실패한다. */
  @DisplayName("토큰이 위변조 되었을 경우")
  @Test
  void badToken() {
    String accessToken = "xxxx.yyyyy.zzzzz";

    var response = 내_정보_조회_요청(accessToken);

    인증_실패함(response);
  }

  /** When 인증 코드를 사용해서 로그인 요청을 하면 Then 액세스 토큰이 부여되고 AND AND 토큰을 사용해서 내 정보를 조회할 수 있다. */
  @DisplayName("깃헙 인증")
  @Test
  void githubAuth() {
    ExtractableResponse<Response> response = 깃헙_로그인_요청(사용자.getCode());

    String accessToken = 깃헙_로그인됨(response);

    ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회_요청(accessToken);
    내_정보_조회됨(내_정보_조회_응답, 사용자.getMember());
  }
}
