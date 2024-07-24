package nextstep.subway.acceptance.line.steps;

import static nextstep.subway.acceptance.line.steps.LineAcceptanceSteps.지하철_노선_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.line.application.dto.AppendLineSectionRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.domain.Station;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class LineSectionAcceptanceSteps {
  private LineSectionAcceptanceSteps() {}

  public static ExtractableResponse<Response> 노선_구간_등록_요청(Line line, LineSection lineSection) {
    AppendLineSectionRequest request =
        new AppendLineSectionRequest(
            lineSection.getUpStation().getId(),
            lineSection.getDownStation().getId(),
            lineSection.getDistance());
    return RestAssured.given()
        .log()
        .all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(request)
        .when()
        .post("/lines/" + line.getId() + "/sections")
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 노선_첫_구간으로_등록됨(
      ExtractableResponse<Response> response, Line line, LineSection lineSection) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds.get(0)).isEqualTo(lineSection.getDownStation().getId());
  }

  public static void 노선_마지막_구간으로_등록됨(
      ExtractableResponse<Response> response, Line line, LineSection lineSection) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds.get(stationIds.size() - 1))
        .isEqualTo(lineSection.getDownStation().getId());
  }

  public static void 노선_i변째_구간으로_등록됨(
      ExtractableResponse<Response> response, Line line, LineSection lineSection, int i) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds.get(i)).isEqualTo(lineSection.getDownStation().getId());
  }

  public static void 노선_구간_요청_실패함(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  public static ExtractableResponse<Response> 노선_구간_삭제_요청(Line line, Station station) {
    String uri = String.format("/lines/%d/sections", line.getId());
    return RestAssured.given()
        .log()
        .all()
        .queryParam("stationId", station.getId())
        .when()
        .delete(uri)
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 노선_구간_삭제됨(ExtractableResponse<Response> response, Line line, Station station) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds).isNotEmpty().doesNotContain(station.getId());
  }

  public static void 노선_구간_삭제_실패함(ExtractableResponse<Response> response, HttpStatus httpStatus) {
    assertThat(response.statusCode()).isEqualTo(httpStatus.value());
  }
}
