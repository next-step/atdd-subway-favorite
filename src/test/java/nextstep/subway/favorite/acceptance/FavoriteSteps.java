package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_조회_실패_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .params("source", source)
                .params("target", target)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청되어_있음(Long source, Long target, String accessToken) {
        return 즐겨찾기_생성_요청(source, target, accessToken);
    }
}
