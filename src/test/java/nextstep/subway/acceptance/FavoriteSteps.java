package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTest.ADMIN_ACCESS_TOKEN;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 로그인후_즐겨찾기_생성(long source, long target) {
        return secureGiven()
                .body(Map.of(
                        "source", source,
                        "target", target
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인후_즐겨찾기_조회(ExtractableResponse<Response> response) {
        String location = response.header("Location");

        return secureGiven()
                .when().get(location)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인후_즐겨찾기_삭제(ExtractableResponse<Response> response) {
        String location = response.header("Location");

        return secureGiven()
                .when().delete(location)
                .then().log().all()
                .extract();
    }


    private static RequestSpecification secureGiven() {
        return RestAssured.given().log().all()
                .auth().oauth2(ADMIN_ACCESS_TOKEN);
    }
}
