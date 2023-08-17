package nextstep.favorite.acceptance.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_AUTH_SUFFIX = "bearer ";

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long id) {
        return RestAssured.given().log().all()
                .header(HEADER_AUTHORIZATION, HEADER_AUTH_SUFFIX + accessToken)
                .delete("/favorites/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .header(HEADER_AUTHORIZATION, HEADER_AUTH_SUFFIX + accessToken)
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, Long> param = new HashMap<>();
        param.put("source", source);
        param.put("target", target);

        return RestAssured.given().log().all()
                .header(HEADER_AUTHORIZATION, HEADER_AUTH_SUFFIX + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static FavoriteResponse 즐겨찾기_생성_응답(String accessToken, Long source, Long target) {
        return 즐겨찾기_생성_요청(accessToken, source, target).as(FavoriteResponse.class);
    }
}
