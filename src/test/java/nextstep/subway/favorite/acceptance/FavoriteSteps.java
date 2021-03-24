package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    private static final String URI_FAVORITES = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, StationResponse source, StationResponse target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source.getId()));
        params.put("target", String.valueOf(target.getId()));

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(URI_FAVORITES)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(uri)
                .then().log().all().extract();
    }
}
