package nextstep.favorite.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class FavoriteAcceptanceSteps {
  private FavoriteAcceptanceSteps() {}

  public static ExtractableResponse<Response> 즐겨찾기_생성_요청(
      Station source, Station target, String accessToken) {
    FavoriteRequest request = new FavoriteRequest(source.getId(), target.getId());
    return RestAssured.given()
        .log()
        .all()
        .auth()
        .oauth2(accessToken)
        .body(request)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/favorites")
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
    return RestAssured.given()
        .log()
        .all()
        .auth()
        .oauth2(accessToken)
        .when()
        .get("/favorites")
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 즐겨찾기_목록에_포함됨(
      ExtractableResponse<Response> response,
      List<ExtractableResponse<Response>> createResponses) {
    List<FavoriteResponse> actualFavorites =
        response.jsonPath().getList(".", FavoriteResponse.class);
    List<FavoriteResponse> expectedFavorites =
        createResponses.stream()
            .map(it -> it.as(FavoriteResponse.class))
            .collect(Collectors.toList());
    assertThat(actualFavorites).containsExactlyInAnyOrderElementsOf(expectedFavorites);
  }

  public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String uri, String accessToken) {
    return RestAssured.given().log().all().when().delete(uri).then().log().all().extract();
  }

  public static void 즐겨찾기_삭제됨(String uri, ExtractableResponse<Response> response, String accessToken) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    ExtractableResponse<Response> getResponse =
        RestAssured.given().log().all().when().get(uri).then().log().all().extract();
    assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }
}
