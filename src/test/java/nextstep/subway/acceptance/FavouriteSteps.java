package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavouriteSteps {
    private static final String SOURCE = "source";
    private static final String TARGET = "target";

    public static ExtractableResponse<Response> 즐겨찾기_요청(String 사용자토큰, Long 상행역Id, Long 하행역Id) {
        Map<String, Long> requestBody = new HashMap<>(2);
        requestBody.put(SOURCE, 상행역Id);
        requestBody.put(TARGET, 하행역Id);

        return RestAssured
                .given().log().all()
                .auth().preemptive().oauth2(사용자토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)

                .when()
                .post("/favourites")

                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String 사용자토큰) {
        return RestAssured
                .given().log().all()
                .auth().preemptive().oauth2(사용자토큰)
                .accept(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/favourites")

                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_취소(String 사용자토큰, Long 선호경로Id) {
        return RestAssured
                .given().log().all()
                .auth().preemptive().oauth2(사용자토큰)

                .when()
                .delete("/favourites/" + 선호경로Id)

                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 미인증_즐겨찾기_요청(Long 상행역Id, Long 하행역Id) {
        Map<String, Long> requestBody = new HashMap<>(2);
        requestBody.put(SOURCE, 상행역Id);
        requestBody.put(TARGET, 하행역Id);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)

                .when()
                .post("/favourites")

                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 미인증_즐겨찾기_조회() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/favourites")

                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 미인증_즐겨찾기_취소() {
        return RestAssured
                .given().log().all()

                .when()
                .delete("/favourites/" + 1)

                .then().log().all()
                .extract();
    }
}
