package nextstep.favorite.accpetance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    private FavoriteSteps() {
    }

    public static ExtractableResponse<Response> 즐겨찾기_경로_등록(String accessToken, String sourceStationName, String targetStationName,
                                                           Map<String, Long> stationIdByName) {
        return 즐겨찾기_경로_등록(accessToken, sourceStationName, targetStationName, stationIdByName, HttpStatus.CREATED);
    }

    public static ExtractableResponse<Response> 즐겨찾기_경로_등록(String accessToken, String sourceStationName, String targetStationName,
                                                           Map<String, Long> stationIdByName, HttpStatus expectedStats) {
        final Map<String, Long> param = new HashMap<>();
        param.put("source", stationIdByName.get(sourceStationName));
        param.put("target", stationIdByName.get(targetStationName));

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(expectedStats.value())
                .extract();
    }

    public static void 즐겨찾기_경로_등록됨(String accessToken, String sourceStationName, String targetStationName, int order) {
        final JsonPath jsonPath = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();

        final String source = String.format("[%d].source.name", order);
        final String target = String.format("[%d].target.name", order);

        Assertions.assertEquals(sourceStationName, jsonPath.getString(source));
        Assertions.assertEquals(targetStationName, jsonPath.getString(target));
    }

    public static void 즐겨찾기_경로_존재하지않음(String accessToken) {
        final JsonPath jsonPath = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();

        Assertions.assertEquals(0, jsonPath.getList("").size());
    }

    public static void 즐겨찾기_경로_삭제(String 사용자1_토큰, ExtractableResponse<Response> response) {
        final String uri = response.header("Location");

        RestAssured.given().log().all()
                .auth().oauth2(사용자1_토큰)
                .when().delete(uri)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
