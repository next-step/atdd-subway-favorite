package nextstep.favorite.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String token, Map<String, String> body) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, String path) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(path)
            .then().log().all()
            .extract();
    }

    public static List<Long> 즐겨찾기_목록_조회_응답에서_아이디_목록_추출(ExtractableResponse<Response> 즐겨찾기_목록_응답) {
        return 즐겨찾기_목록_응답.jsonPath()
            .getList("id", Long.class);
    }


    public static Map<String, String> 즐겨찾기_등록_본문(Long sourceId, Long targetId) {
        return Map.of(
            "source", sourceId + "",
            "target", targetId + ""
        );

    }

    public static String 즐겨찾기_등록_응답에서_헤더_추출(ExtractableResponse<Response> 즐겨찾기_등록_응답, String name) {
        return 즐겨찾기_등록_응답.header(name);
    }


}
