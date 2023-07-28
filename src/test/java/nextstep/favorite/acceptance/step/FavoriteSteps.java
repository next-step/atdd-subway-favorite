package nextstep.favorite.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class FavoriteSteps {
    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";

    private FavoriteSteps() {
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long sourceStationId, Long targetStationId) {
        Map<String, Long> params = Map.of(
                "source", sourceStationId,
                "target", targetStationId
        );

        return RestAssured.given().log().all()
                .header("authorization", ACCESS_TOKEN_PREFIX + accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
