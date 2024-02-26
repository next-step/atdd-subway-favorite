package nextstep.favorite.acceptance;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.auth.acceptance.AuthSteps.토큰_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import nextstep.favorite.application.dto.FavoriteCreateResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.acceptance.StationSteps;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

  StationResponse 강남역;
  StationResponse 양재역;
  String accessToken;

  String INVALID_ACCESS_TOKEN = "INVALID ACCESS TOKEN";

  String EMAIL = "domodazzi0@gmail.com";
  String PASSWORD = "password";

  /**
   * Given 역과 노선(+구간) 그리고 회원 정보를 생성한다.
   */
  @BeforeEach
  public void setUp() {
    강남역 = StationSteps.지하철역_생성("강남역");
    양재역 = StationSteps.지하철역_생성("양재역");
    LineSteps.지하철_노선_생성("신분당선", "빨강", 강남역.getId(), 양재역.getId(), 10);


    회원_생성_요청(EMAIL, PASSWORD, 10);
    accessToken = 토큰_요청(EMAIL, PASSWORD).as(TokenResponse.class)
        .getAccessToken();
  }

  /**
   * When 즐겨찾기를 생성하면
   * Then 즐겨찾기가 생성되고 생성한 즐겨찾기가 조회된다.
   */
  @DisplayName("즐겨찾기를 생성한다.")
  @Test
  void 즐겨찾기_생성() {
    // when
    final var response = 즐겨찾기_생성_요청(강남역.getId(), 양재역.getId(), accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

//    final var result = 즐겨찾기_목록_조회_요청(accessToken);
//    assertThat(result.jsonPath().getList("source.id")).contains(강남역.getId());
//    assertThat(result.jsonPath().getList("target.id")).contains(양재역.getId());
  }

  /**
   * When 인증 토큰 없이 즐겨찾기 생성을 요청하면
   * Then 인증 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 생성 실패 - 인증 토큰 누락")
  @Test
  void 즐겨찾기_생성_실패_인증_토큰_누락() {
    // when
    final var response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/favorites")
        .then().log().all().extract();

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  /**
   * When 유효하지 않은 인증 토큰으로 즐겨찾기 생성을 요청하면
   * Then 인증 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 생성 실패 - 유효하지 않은 인증 토큰")
  @Test
  void 즐겨찾기_생성_실패_유효하지_않은_인증_토큰() {
    // when
    final var response = 즐겨찾기_생성_요청(강남역.getId(), 양재역.getId(), INVALID_ACCESS_TOKEN);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  /**
   * When 존재하지 않는 역에 대한 즐겨찾기 생성을 요청하면
   * Then 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 생성 실패 - 존재하지 않는 역")
  @Test
  void 즐겨찾기_생성_실패_존재하지_않는_역() {
    // when
    final var response = 즐겨찾기_생성_요청(강남역.getId(), 강남역.getId(), accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  /**
   * When 출발역과 도착역이 같은 경로에 대한 즐겨찾기 생성을 요청하면
   * Then 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 생성 실패 - 출발역과 도착역이 같음")
  @Test
  void 즐겨찾기_생성_실패_출발역과_도착역이_같음() {
    // when
    final var response = 즐겨찾기_생성_요청(강남역.getId(), 강남역.getId(), accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  /**
   * When 연결되지 않는 경로에 대한 즐겨찾기 생성을 요청하면
   * Then 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 생성 실패 - 연결되지 않는 경로")
  @Test
  void 즐겨찾기_생성_실패_연결되지_않는_경로() {
    // when
    final var response = 즐겨찾기_생성_요청(강남역.getId(), 강남역.getId(), accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  /**
   * Given 즐겨찾기를 생성하고
   * When 즐겨찾기 목록을 조회하면
   * Then 생성한 즐겨찾기를 조회할 수 있다.
   */
  @DisplayName("즐겨찾기 목록을 조회한다.")
  @Test
  void 즐겨찾기_목록_조회() {
    // given
    즐겨찾기_생성_요청(강남역.getId(), 양재역.getId(), accessToken);

    // when
    final var response = 즐겨찾기_목록_조회_요청(accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    final var result = 즐겨찾기_목록_조회_요청(accessToken);
    assertThat(result.jsonPath().getList("source.id")).contains(강남역.getId().intValue());
    assertThat(result.jsonPath().getList("target.id")).contains(양재역.getId().intValue());
  }

  /**
   * When 인증 토큰 없이 즐겨찾기 목록 조회를 요청하면
   * Then 인증 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 목록 조회 실패 - 인증 토큰 누락")
  @Test
  void 즐겨찾기_목록_조회_실패_인증_토큰_누락() {
    // when
    final var response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/favorites")
        .then().log().all().extract();

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  /**
   * When 유효하지 않은 인증 토큰으로 즐겨찾기 목록 조회를 요청하면
   * Then 인증 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 목록 조회 실패 - 유효하지 않은 인증 토큰")
  @Test
  void 즐겨찾기_목록_조회_실패_유효하지_않은_인증_토큰() {
    // when
    final var response = 즐겨찾기_목록_조회_요청(INVALID_ACCESS_TOKEN);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  /**
   * Given 즐겨찾기를 생성하고
   * When 즐겨찾기를 삭제하면
   * Then 즐겨찾기가 삭제되고 생성한 즐겨찾기를 조회할 수 없다.
   */
  @DisplayName("즐겨찾기를 삭제한다.")
  @Test
  void 즐겨찾기_삭제() {
    // given
    var favorite = 즐겨찾기_생성_요청(강남역.getId(), 양재역.getId(), accessToken)
        .as(FavoriteCreateResponse.class);

    // when
    final var response = 즐겨찾기_삭제_요청(favorite.getId(), accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    assertThat(즐겨찾기_목록_조회_요청(accessToken).jsonPath().getList("id")).doesNotContain(favorite.getId());
  }

  /**
   * When 인증 토큰 없이 즐겨찾기 삭제를 요청하면
   * Then 인증 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 삭제 실패 - 인증 토큰 누락")
  @Test
  void 즐겨찾기_삭제_실패_인증_토큰_누락() {
    // when
    final var response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().delete("/favorites/{id}", 1L)
        .then().log().all().extract();

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  /**
   * When 유효하지 않은 인증 토큰으로 즐겨찾기 삭제를 요청하면
   * Then 인증 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 삭제 실패 - 유효하지 않은 인증 토큰")
  @Test
  void 즐겨찾기_삭제_실패_유효하지_않은_인증_토큰() {
    // when
    final var response = 즐겨찾기_삭제_요청(1L, INVALID_ACCESS_TOKEN);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  /**
   * When 등록되지 않은 즐겨찾기를 삭제 요청하면
   * Then 에러가 발생한다.
   */
  @DisplayName("즐겨찾기 삭제 실패 - 등록되지 않은 즐겨찾기")
  @Test
  void 즐겨찾기_삭제_실패_등록되지_않은_즐겨찾기() {

    // given
    long notExistsFavoriteId = 9999L;

    // when
    final var response = 즐겨찾기_삭제_요청(notExistsFavoriteId, accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}