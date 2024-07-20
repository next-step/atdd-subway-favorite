package nextstep.subway.acceptance.line.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.application.dto.CreateLineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.UpdateLineRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class LineAcceptanceSteps {
  private LineAcceptanceSteps() {}

  public static ExtractableResponse<Response> 지하철_노선_생성_요청(Line line) {
    LineSection section = line.getLineSections().getFirst();
    CreateLineRequest request =
        new CreateLineRequest(
            line.getName(),
            line.getColor(),
            section.getUpStation().getId(),
            section.getDownStation().getId(),
            section.getDistance());
    return RestAssured.given()
        .log()
        .all()
        .body(request)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/lines")
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();
  }

  public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
    return RestAssured.given().log().all().when().get("/lines").then().log().all().extract();
  }

  public static void 지하철_노선_목록에_포함됨(
      ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createResponses) {
    List<LineResponse> actualLines = response.jsonPath().getList(".", LineResponse.class);
    List<LineResponse> expectedLines =
        createResponses.stream().map(it -> it.as(LineResponse.class)).collect(Collectors.toList());
    assertThat(actualLines).containsExactlyInAnyOrderElementsOf(expectedLines);
  }

  public static ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
    return RestAssured.given().log().all().when().get(uri).then().log().all().extract();
  }

  public static void 지하철_노선_조회됨(ExtractableResponse<Response> response, Line line) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.as(LineResponse.class)).isEqualTo(LineResponse.from(line));
  }

  public static ExtractableResponse<Response> 지하철_노선_수정_요청(String uri, String name, String color) {
    return RestAssured.given()
        .log()
        .all()
        .body(new UpdateLineRequest(name, color))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .put(uri)
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 지하철_노선_수정됨(String uri, String newName, String newColor) {
    LineResponse updatedLine = 지하철_노선_조회_요청(uri).as(LineResponse.class);
    assertThat(updatedLine.getName()).isEqualTo(newName);
    assertThat(updatedLine.getColor()).isEqualTo(newColor);
  }

  public static ExtractableResponse<Response> 지하철_삭제_요청(String uri) {
    return RestAssured.given().log().all().when().delete(uri).then().log().all().extract();
  }

  public static void 지하철_노선_삭제됨(String uri, ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    ExtractableResponse<Response> getResponse =
        RestAssured.given().log().all().when().get(uri).then().log().all().extract();
    assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }
}
