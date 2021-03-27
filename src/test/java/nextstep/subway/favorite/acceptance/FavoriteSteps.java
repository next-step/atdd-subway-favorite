package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, FavoriteRequest request) {
        return RestAssured.given().log().all()
                    .auth().oauth2(tokenResponse.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/favorites/")
                    .then().log().all()
                    .extract();

    }
    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }
    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, int expectedSize) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favorites = response.body().jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favorites.size()).isEqualTo(expectedSize);
    }

    public static void 즐겨찾기_목록_조회_실패됨(ExtractableResponse<Response> response) {}

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }
    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
