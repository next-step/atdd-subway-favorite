package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceTest extends AcceptanceTest {


  private Long 강남역;
  private Long 양재역;

  @BeforeEach
  public void setUp() {
    super.setUp();

    강남역 = 지하철역_생성_요청(관리자토큰, "강남역").jsonPath().getLong("id");
    양재역 = 지하철역_생성_요청(관리자토큰, "양재역").jsonPath().getLong("id");
  }

  /**
   * When 로그인한 회원이 즐겨찾기를 등록하면
   * Then 즐겨찾기가 추가된다
   */
  @Test
  void 즐겨찾기_생성() {
    Map<String, Long> params = new HashMap<>();
    params.put("source", 강남역);
    params.put("target", 양재역);
    ExtractableResponse<Response> response = CommonAuthRestAssured.given(관리자토큰)
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/favorites")
        .then().log().all()
        .extract();

    assertAll(
        () -> assertEquals(response.statusCode(), HttpStatus.CREATED.value())
    );

    ExtractableResponse<Response> result = CommonAuthRestAssured.given(관리자토큰)
        .when().get("/favorites")
        .then().log().all().extract();

    List<String> source = result.jsonPath().getList("source.name");
    List<String> target = result.jsonPath().getList("target.name");

    assertAll(
        () -> assertEquals(result.statusCode(), HttpStatus.OK.value()),
        () -> assertThat(source).containsExactly("강남역"),
        () -> assertThat(target).containsExactly("양재역")
    );
  }

  @Test
  void 즐겨찾기_생성_미로그인_권한_에러() {
    Map<String, Long> params = new HashMap<>();
    params.put("source", 강남역);
    params.put("target", 양재역);
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/favorites")
        .then().log().all()
        .extract();

    assertAll(
        () -> assertEquals(response.statusCode(), HttpStatus.UNAUTHORIZED.value())
    );
  }

  @Test
  void 즐겨찾기_생성_같은역_등록_에러() {
    Map<String, Long> params = new HashMap<>();
    params.put("source", 강남역);
    params.put("target", 강남역);
    ExtractableResponse<Response> result = CommonAuthRestAssured.given(관리자토큰)
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/favorites")
        .then().log().all()
        .extract();

    assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void 즐겨찾기_조회_미로그인_권한_에러() {
    ExtractableResponse<Response> result = RestAssured
        .given().log().all()
        .when().get("/favorites")
        .then().log().all().extract();

    assertAll(
        () -> assertEquals(result.statusCode(), HttpStatus.UNAUTHORIZED.value())
    );
  }

  @Test
  void 즐겨찾기_삭제() {
    Map<String, Long> params = new HashMap<>();
    params.put("source", 강남역);
    params.put("target", 양재역);
    ExtractableResponse<Response> response = CommonAuthRestAssured.given(관리자토큰)
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/favorites")
        .then().log().all()
        .extract();

    String location = response.header("Location");

    ExtractableResponse<Response> result = CommonAuthRestAssured.given(관리자토큰)
        .when().delete(location)
        .then().log().all().extract();

    assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void 즐겨찾기_삭제_미로그인_권한_에러() {
    ExtractableResponse<Response> result = RestAssured
        .given().log().all()
        .when().delete("/favorites/" + 1L)
        .then().log().all().extract();

    assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }
}
