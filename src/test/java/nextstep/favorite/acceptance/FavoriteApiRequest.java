package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import nextstep.favorite.payload.FavoriteRequest;
import org.springframework.http.MediaType;

public class FavoriteApiRequest {


    public static Response 즐겨찾기를_생성한다(final String accessToken, final Long 출발역, final Long 도착역) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(new FavoriteRequest(출발역, 도착역))
                .when().post("/favorites")
                .then().log().all()
                .extract().response();
    }

    public static Response 특정회원의_즐겨찾기를_전체_조회한다(final String accessToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract().response();
    }

    public static Response 즐겨찾기를_삭제한다(final String accessToken, final Long number) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().post("/favorites/{id}", number)
                .then().log().all()
                .extract().response();
    }


}
