package nextstep.subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteStep {

    public static void 즐겨찾기_생성(String accessToken, FavoriteRequest favoriteRequest) {
        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value()).extract();
    }
}
