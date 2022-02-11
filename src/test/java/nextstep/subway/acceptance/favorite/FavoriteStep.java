package nextstep.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;

public class FavoriteStep {
    private FavoriteStep() {}

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(String accessToken, long source, long target) {
        return RestAssured.given().log().all()
                          .auth().oauth2(accessToken)
                          .body(new FavoriteRequest(source, target))
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/favorites")
                          .then().log().all()
                          .extract();
    }

    public static void 즐겨찾기_추가_요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("location")).isNotBlank();
    }

    public static Long 즐겨찾기_추가_요청_하고_ID_반환(String accessToken, long source, long target) {
        ExtractableResponse<Response> createResponse = 즐겨찾기_추가_요청(accessToken, source, target);
        즐겨찾기_추가_요청_성공(createResponse);
        return createResponse.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                          .auth().oauth2(accessToken)
                          .when().get("/favorites")
                          .then().log().all()
                          .extract();
    }

    public static void 즐겨찾기_목록_조회_요청_성공(ExtractableResponse<Response> response, long id, long source, long target) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath()
                           .getList("id", Long.class)
                           .get(0)).isEqualTo(id);
        assertThat(response.jsonPath()
                           .getList("source", Long.class)
                           .get(0)).isEqualTo(source);
        assertThat(response.jsonPath()
                           .getList("target", Long.class)
                           .get(0)).isEqualTo(target);
    }
}
