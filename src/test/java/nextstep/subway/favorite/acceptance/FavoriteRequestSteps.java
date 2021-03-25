package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class FavoriteRequestSteps {

    public static ExtractableResponse<Response> 지하철_즐겨찾기_추가_요청(TokenResponse tokenResponse, StationResponse target, StationResponse source) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(target, source);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
