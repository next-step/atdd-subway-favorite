package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

class FavoriteAcceptanceTest extends AcceptanceTest {

  public static final String EMAIL = "email@email.com";
  public static final String PASSWORD = "password";
  public static final int AGE = 20;

  private Long 교대역;
  private Long 강남역;
  private Long 양재역;
  private Long 남부터미널역;
  private Long 이호선;
  private Long 신분당선;
  private Long 삼호선;
  private String 로그인_토큰;

  /*
  Background
  Given 지하철역 등록되어 있음
  And 지하철 노선 등록되어 있음
  And 지하철 노선에 지하철역 등록되어 있음
  And 회원 등록되어 있음
  And 로그인 되어있음
  */
  @BeforeEach
  void setup() {

    교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
    강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
    양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
    남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");


    이호선 = 지하철_노선_생성_요청(createLineCreateParams("2호선", "green", 교대역, 강남역, 10)).jsonPath().getLong("id");
    신분당선 = 지하철_노선_생성_요청(createLineCreateParams("신분당선", "red", 강남역, 양재역, 10)).jsonPath().getLong("id");
    삼호선 = 지하철_노선_생성_요청(createLineCreateParams("3호선", "orange", 교대역, 남부터미널역, 2)).jsonPath().getLong("id");

    지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));

    회원_생성_요청(EMAIL, PASSWORD, AGE);
    로그인_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
  }

  void addFavorite() {
    // When
    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(로그인_토큰, 교대역, 강남역);
    // Then
    즐겨찾기_생성됨(response);
  }

  void getFavorite() {
    // Given
    즐겨찾기_생성_요청(로그인_토큰, 교대역, 강남역);

    // When
    ExtractableResponse<Response> queryResponse = 즐겨찾기_목록_조회_요청(로그인_토큰);
    // Then
    즐겨찾기_목록_조회됨(queryResponse, 교대역, 강남역);
  }

  void deleteFavorite() {
    // Given
    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(로그인_토큰, 교대역, 강남역);
    Long createdId = createResponse.jsonPath().getLong("id");

    // When
    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(로그인_토큰, createdId);
    // Then
    즐겨찾기_삭제됨(response);
  }

  @DisplayName("즐겨찾기를 관리")
  @Test
  void manageFavorites() {
    // When
    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(로그인_토큰, 교대역, 강남역);
    // Then
    즐겨찾기_생성됨(createResponse);

    // When
    ExtractableResponse<Response> queryResponse = 즐겨찾기_목록_조회_요청(로그인_토큰);
    // Then
    즐겨찾기_목록_조회됨(queryResponse, 교대역, 강남역);

    Long createdId = createResponse.jsonPath().getLong("id");

    // When
    ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(로그인_토큰, createdId);
    // Then
    즐겨찾기_삭제됨(deleteResponse);
  }

  private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
    Map<String, String> params = new HashMap<>();
    params.put("upStationId", upStationId + "");
    params.put("downStationId", downStationId + "");
    params.put("distance", distance + "");
    return params;
  }
}
