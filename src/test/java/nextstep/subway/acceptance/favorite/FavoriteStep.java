package nextstep.subway.acceptance.favorite;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;

public class FavoriteStep {
    private FavoriteStep() {}

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(String accessToken, FavoriteRequest request) {
        return RestAssured.given().log().all()
                          .auth().oauth2(accessToken)
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/favorites")
                          .then().log().all()
                          .extract();
    }

    public static void 즐겨찾기_추가_요청_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                          .auth().oauth2(accessToken)
                          .when().get("/favorites")
                          .then().log().all()
                          .extract();
    }
}
