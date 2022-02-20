package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;

class MemberAcceptanceTest extends AcceptanceTest {
  public static final String EMAIL = "email@email.com";
  public static final String PASSWORD = "password";
  public static final int AGE = 20;

  @DisplayName("회원가입을 한다.")
  @Test
  void createMember() {
    // when
    ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

    // then
    회원_생성됨(response);
  }

  @DisplayName("회원 정보를 조회한다.")
  @Test
  void getMember() {
    // given
    ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

    // when
    ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

    // then
    회원_정보_조회됨(response, EMAIL, AGE);

  }

  @DisplayName("회원 정보를 수정한다.")
  @Test
  void updateMember() {
    // given
    ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

    // when
    ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

    // then
    회원_정보_수정됨(response);
  }

  @DisplayName("회원 정보를 삭제한다.")
  @Test
  void deleteMember() {
    // given
    ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

    // when
    ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

    // then
    회원_삭제됨(response);
  }

  @DisplayName("회원 정보를 관리한다.")
  @Test
  void manageMember() {
    // When
    ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

    // Then
    회원_생성됨(createResponse);

    // When
    ExtractableResponse<Response> searchResponse = 회원_정보_조회_요청(createResponse);

    // Then
    회원_정보_조회됨(searchResponse, EMAIL, AGE);

    // When
    ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

    // Then
    회원_정보_수정됨(updateResponse);

    // When
    ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);

    // Then
    회원_삭제됨(deleteResponse);
  }

  @DisplayName("나의 정보를 관리한다.")
  @Test
  void manageMyInfo() {
    ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
    회원_생성됨(회원_생성_응답);

    String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

    ExtractableResponse<Response> 내_회원_정보_조회_응답 = 내_회원_정보_조회_요청(accessToken);
    회원_정보_조회됨(내_회원_정보_조회_응답, EMAIL, AGE);

    ExtractableResponse<Response> 내_회원_정보_수정_응답 = 내_회원_정보_수정_요청(accessToken, "new" + EMAIL, "new" + PASSWORD, AGE);
    회원_정보_수정됨(내_회원_정보_수정_응답);

    ExtractableResponse<Response> 내_회원_정보_삭제_응답 = 내_회원_삭제_요청(accessToken);
    회원_삭제됨(내_회원_정보_삭제_응답);
  }
}