package nextstep.favorite.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class FavoriteSteps {
    public enum TokenType {
        BEARER("Bearer "),
        BASIC("Basic "),;

        private final String prefix;

        TokenType(String prefix) {
            this.prefix = prefix;
        }
    }

    private FavoriteSteps() {
    }

    public static ExtractableResponse<Response> 토큰_헤더_없이_즐겨찾기_생성_요청(Long sourceStationId, Long targetStationId) {
        Map<String, Long> params = createParams(sourceStationId, targetStationId);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenType type, String accessToken, Long sourceStationId, Long targetStationId) {
        Map<String, Long> params = createParams(sourceStationId, targetStationId);

        return RestAssured.given().log().all()
                .header("authorization", type.prefix + accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private static Map<String, Long> createParams(Long sourceStationId, Long targetStationId) {
        return Map.of(
                "source", sourceStationId,
                "target", targetStationId
        );
    }
}
