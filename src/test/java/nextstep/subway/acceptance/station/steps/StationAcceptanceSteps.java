package nextstep.subway.acceptance.station.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class StationAcceptanceSteps {
  private StationAcceptanceSteps() {}

  public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
    Map<String, String> params = new HashMap<>();
    params.put("name", name);

    return RestAssured.given()
        .log()
        .all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/stations")
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 지하철역_생성됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
    return RestAssured.given().log().all().when().get("/stations").then().log().all().extract();
  }

  public static void 지하철역_목록_조회됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  public static void 지하철역_목록에_포함됨(ExtractableResponse<Response> response, String... names) {
    List<String> stationNames = response.jsonPath().getList("name", String.class);
    assertThat(stationNames).containsExactlyInAnyOrder(names);
  }

  public static void 지하철역_목록에_포함되지_않음(ExtractableResponse<Response> response, String... names) {
    List<String> stationNames = response.jsonPath().getList("name", String.class);
    assertThat(stationNames).doesNotContain(names).isEmpty();
  }

  public static ExtractableResponse<Response> 지하철역_삭제_요청(String uri) {
    return RestAssured.given().log().all().when().delete(uri).then().log().all().extract();
  }

  public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}
