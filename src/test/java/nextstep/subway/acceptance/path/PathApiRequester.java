package nextstep.subway.acceptance.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PathApiRequester {

    public static ExtractableResponse<Response> getPath(Long 출발역id, Long 도착역id) {
        return given().log().all()
                .queryParam("source", 출발역id)
                .queryParam("target", 도착역id)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
