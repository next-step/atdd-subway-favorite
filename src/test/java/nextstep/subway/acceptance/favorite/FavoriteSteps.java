package nextstep.subway.acceptance.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.favorite.FavoriteRequest;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(FavoriteRequest request) {
        return RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                .when().log().all()
                    .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String url) {
        return RestAssured
                .given().log().all()
                .when().log().all()
                    .get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String url) {
        return RestAssured
                .given().log().all()
                .when().log().all()
                    .delete(url)
                .then().log().all()
                .extract();
    }
}