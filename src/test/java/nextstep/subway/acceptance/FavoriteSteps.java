package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

  public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long sourceId, Long targetId) {
    Map<String, Long> params = new HashMap<>();
    params.put("source", sourceId);
    params.put("target", targetId);

    return CommonAuthRestAssured.given(token)
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/favorites")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
    return CommonAuthRestAssured.given(token)
        .when()
        .get("/favorites")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> response) {
    String location = response.header("Location");
    return CommonAuthRestAssured.given(token)
        .when()
        .delete(location)
        .then().log().all()
        .extract();
  }
}
