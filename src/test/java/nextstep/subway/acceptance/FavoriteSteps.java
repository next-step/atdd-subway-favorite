package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {
  public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
    Map<String, String> params = new HashMap<>();
    params.put("source", source + "");
    params.put("color", target + "");
    return RestAssured
      .given().log().all()
      .auth().oauth2(accessToken)
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(params)
      .when().post("/favorites")
      .then().log().all().extract();
  }

  public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
    return RestAssured.given().log().all()
      .auth().oauth2(accessToken)
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .when().get("/favorites")
      .then().log().all()
      .statusCode(HttpStatus.OK.value())
      .extract();
  }

  public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, Long sourceId, Long targetId) {
    assertThat(response.jsonPath().getString("id")).isNotNull();
    assertThat(response.jsonPath().getLong("source.id")).isEqualTo(sourceId);
    assertThat(response.jsonPath().getLong("target.id")).isEqualTo(targetId);
  }

  public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long id) {
    return RestAssured
      .given().log().all()
      .auth().oauth2(accessToken)
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .when().delete("/favorites/{id}", id)
      .then().log().all().extract();
  }

  public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}
