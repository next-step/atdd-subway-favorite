package nextstep.favorite.acceptance;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.member.acceptance.AuthSteps.토큰_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.acceptance.AuthSteps;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.acceptance.StationSteps;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

  StationResponse 강남역;
  StationResponse 양재역;
  String accessToken;

  String EMAIL = "domodazzi0@gmail.com";
  String PASSWORD = "password";

  @Autowired
  private MemberRepository memberRepository;

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

    final var result = 즐겨찾기_목록_조회_요청(accessToken);
    assertThat(result.jsonPath().getList("source.id")).contains(강남역.getId());
    assertThat(result.jsonPath().getList("target.id")).contains(양재역.getId());
  }

  /**
   * Given 즐겨찾기를 생성하고
   * When 즐겨찾기 목록을 조회하면
   * Then 생성한 즐겨찾기를 조회할 수 있다.
   */
  @DisplayName("즐겨찾기를 조회한다.")
  @Test
  void 즐겨찾기_목록_조회() {
    // given
    즐겨찾기_생성_요청(강남역.getId(), 양재역.getId(), accessToken);

    // when
    final var response = 즐겨찾기_목록_조회_요청(accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    final var result = 즐겨찾기_목록_조회_요청(accessToken);
    assertThat(result.jsonPath().getList("source.id")).contains(강남역.getId());
    assertThat(result.jsonPath().getList("target.id")).contains(양재역.getId());
  }

  /**
   * Given 즐겨찾기를 생성하고
   * When 즐겨찾기를 삭제하면
   * Then 즐겨찾기가 삭제되고 생성한 즐겨찾기를 조회할 수 없다.
   */
  @DisplayName("즐겨찾기를 조회한다.")
  @Test
  void 즐겨찾기_삭제() {
    // given
    final var favoriteId = 즐겨찾기_생성_요청(강남역.getId(), 양재역.getId(), accessToken).header("Location");

    // when
    final var response = 즐겨찾기_삭제_요청(Long.valueOf(favoriteId), accessToken);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    assertThat(즐겨찾기_목록_조회_요청(accessToken).jsonPath().getList("id")).doesNotContain(favoriteId);
  }

  public FavoriteAcceptanceTest(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }
}