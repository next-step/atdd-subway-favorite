package nextstep.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target) {
        return AuthSteps.givenUserRole()
                .body(createFavoritesCreateParams(source, target))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청_관리자(Long source, Long target) {
        return AuthSteps.givenAdminRole()
                .body(createFavoritesCreateParams(source, target))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청() {
        return AuthSteps.givenUserRole()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id) {
        return AuthSteps.givenUserRole()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    public static Map<String, String> createFavoritesCreateParams(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");
        return params;
    }
}
