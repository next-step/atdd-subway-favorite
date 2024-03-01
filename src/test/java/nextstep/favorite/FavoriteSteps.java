package nextstep.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 토큰_없이_즐겨찾기_생성_요청(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_조회됨(ExtractableResponse<Response> response, Long source, Long target) {
        assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(source);
        assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(target);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, String accessToken) {
        String uri = response.header("Location");
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 토큰_없이_즐겨찾기_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured.given().log().all()
                .when().delete(uri)
                .then().log().all().extract();
    }

}