package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PathCommonApi {
    public static ExtractableResponse<Response> findLinePath(Long source, Long target) {
        return RestAssured.given().log().all()
                .queryParam("source", source)
                .queryParam("target", target)
                .get("paths")
                .then().log().all()
                .extract();
    }
}
