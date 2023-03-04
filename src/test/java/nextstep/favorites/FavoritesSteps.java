package nextstep.favorites;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorites.application.dto.FavoriteRequest;
import nextstep.favorites.domain.Favorite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoritesSteps {

    public static ExtractableResponse<Response> 즐겨찾기_추가(String token, Long source, Long target) {
        Map<String, String> map = new HashMap<>();
        map.put("source", source + "");
        map.put("target", target + "");

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(map)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String token) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
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
