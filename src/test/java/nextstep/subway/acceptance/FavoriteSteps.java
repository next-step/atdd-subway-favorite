package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.ADMIN_토큰권한으로_호출;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return ADMIN_토큰권한으로_호출()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회() {
        return ADMIN_토큰권한으로_호출()
                .when()
                .get("/favorites")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(ExtractableResponse<Response> response) {
        return ADMIN_토큰권한으로_호출()
                .when()
                .delete(response.header("Location"))
                .then()
                .log()
                .all()
                .extract();
    }
}
