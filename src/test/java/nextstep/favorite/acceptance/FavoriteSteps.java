package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;

import nextstep.member.acceptance.AuthSteps;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성(String accessToken, Long target, Long source) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("target", target);
        params.put("source", source);

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String accessToken) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String accessToken, Long favoriteId) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .when().delete("/favorites/" + favoriteId)
                .then().log().all()
                .extract();
    }
}
