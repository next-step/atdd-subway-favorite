package nextstep.subway.acceptance.path.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.station.domain.Station;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class PathAcceptanceSteps {
  private PathAcceptanceSteps() {}

  public static ExtractableResponse<Response> 경로_조회_요청(Station source, Station target) {
    PathRequest request = new PathRequest(source.getId(), target.getId());
    return RestAssured.given()
        .log()
        .all()
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .queryParams("source", request.getSource(), "target", request.getTarget())
        .when()
        .get("/paths")
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 경로_역_목록_조회됨(ExtractableResponse<Response> response, String... expectedNames) {
    List<String> stationNames = response.jsonPath().getList("stations.name", String.class);
    assertThat(stationNames).containsExactly(expectedNames);
  }

  public static void 경로_거리_조회됨(ExtractableResponse<Response> response, int expectedDistance) {
    int distance = response.jsonPath().getInt("distance");
    assertThat(distance).isEqualTo(expectedDistance);
  }
}
