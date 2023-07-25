package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

  public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target, String accessToken) {
    Map<String, String> params = new HashMap<>();
    params.put("source", source + "");
    params.put("target", target + "");
    return RestAssured
        .given().log().all()
        .auth().oauth2(accessToken)
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/favorites")
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
    return RestAssured
        .given().log().all()
        .auth().oauth2(accessToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/favorites")
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id, String accessToken) {

    return RestAssured
        .given().log().all()
        .auth().oauth2(accessToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/favorites/{id}", id)
        .then().log().all().extract();
  }

  public static void 즐겨찾기_정보_조회됨(ExtractableResponse<Response> response, Long favoriteId, Long sourceId, Long targetId) {
    assertThat(response.jsonPath().getLong("id")).isEqualTo(favoriteId);
    assertThat(response.jsonPath().getLong("source.id")).isEqualTo(sourceId);
    assertThat(response.jsonPath().getLong("target.id")).isEqualTo(targetId);
  }
}
