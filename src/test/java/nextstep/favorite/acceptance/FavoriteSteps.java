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

    public static ExtractableResponse<Response> 즐겨찾기_생성(String accessToken, Long target, Long Source) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("target", target);
        params.put("Source", Source);

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회() {
        return RestAssured.given().log().all()
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(Long favoriteId) {
        return RestAssured.given().log().all()
                .when().delete("/favorites/" + favoriteId)
                .then().log().all()
                .extract();
    }
}
