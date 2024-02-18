package nextstep.subway.testhelper.apicaller;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.application.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class FavoriteApiCaller {

    public static ExtractableResponse<Response> 즐겨찾기_생성(Map<String, String> params,
                                                        String accessToken) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성(FavoriteRequest request,
                                                        String accessToken) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .body(createParams(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static Map<String, String> createParams(FavoriteRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("source", request.getSource().toString());
        params.put("target", request.getTarget().toString());
        return params;
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String accessToken) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String location,
                                                        String token) {
        return given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
