package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.request.AddFavoriteRequest;
import nextstep.favorite.application.response.AddFavoriteResponse;
import nextstep.favorite.application.response.ShowAllFavoriteResponse;
import nextstep.subway.application.request.AddSectionRequest;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceStep {

    public static ExtractableResponse<Response> 즐겨찾기_추가(AddFavoriteRequest addFavoriteRequest, String accessToken) {
        return RestAssured
                .given().log().all()
                .body(addFavoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가됨(Long startStationId, Long endStationId, String accessToken) {
        return 즐겨찾기_추가(AddFavoriteRequest.of(startStationId, endStationId), accessToken);
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ShowAllFavoriteResponse 즐겨찾기_조회됨(String accessToken) {
        return 즐겨찾기_조회(accessToken).as(ShowAllFavoriteResponse.class);
    }

}
