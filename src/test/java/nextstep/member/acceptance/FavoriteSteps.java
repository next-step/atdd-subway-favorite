package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 비로그인_경로_즐겨찾기_등록_요청(final Long sourceId,
                                                                   final Long targetId) {
        return 경로_즐겨찾기_등록_요청("", sourceId, targetId);
    }

    public static ExtractableResponse<Response> 경로_즐겨찾기_등록_요청(final String token,
                                                              final Long sourceId,
                                                              final Long targetId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
