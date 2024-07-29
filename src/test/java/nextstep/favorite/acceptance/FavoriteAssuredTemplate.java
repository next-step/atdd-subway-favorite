package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import org.springframework.http.MediaType;

public class FavoriteAssuredTemplate {

    public static Response 즐겨찾기_등록(String accessToken, long sourceStationId, long targetStationId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(sourceStationId, targetStationId))
                .when()
                .post("/favorites");
    }

    public static Response 즐겨찾기_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/favorites");
    }

    public static Response 즐겨찾기_석제(String accessToken, Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .pathParam("id", id)
                .when()
                .delete("/favorites/{id}");
    }
}
