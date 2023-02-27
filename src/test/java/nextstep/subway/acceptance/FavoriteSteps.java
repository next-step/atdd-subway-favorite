package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    private FavoriteSteps() {
    }

    public static ExtractableResponse<Response> 즐겨찾기_등록_한다(String accessToken, Long source, Long target) {
        Map<String, Object> params = Map.of(
            "source", source,
            "target", target
        );
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/favorites")
            .then().log().all()
            .extract();
    }

}
