package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class PathSteps {
  public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
    return RestAssured
        .given().log().all()
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/paths?source={sourceId}&target={targetId}", source, target)
        .then().log().all().extract();
  }

  public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance) {
    Map<String, String> lineCreateParams;
    lineCreateParams = new HashMap<>();
    lineCreateParams.put("name", name);
    lineCreateParams.put("color", color);
    lineCreateParams.put("upStationId", upStation + "");
    lineCreateParams.put("downStationId", downStation + "");
    lineCreateParams.put("distance", distance + "");

    return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
  }

  public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
    Map<String, String> params = new HashMap<>();
    params.put("upStationId", upStationId + "");
    params.put("downStationId", downStationId + "");
    params.put("distance", distance + "");
    return params;
  }
}
