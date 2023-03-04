package nextstep.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoritesSteps {

    public static ExtractableResponse<Response> 즐겨찾기_추가(Long source, Long target) {
        Map<String, Long> map = new HashMap<>();
        map.put("source", source);
        map.put("target", target);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(map)
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

    public static ExtractableResponse<Response> 즐겨찾기_삭제(Long id) {

        return RestAssured.given().log().all()
                .when().delete("/favorites/" + id)
                .then().log().all()
                .extract();
    }
}
