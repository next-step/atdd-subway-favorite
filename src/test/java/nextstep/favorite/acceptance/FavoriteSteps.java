package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.TokenRequest;
import nextstep.favorite.application.request.FavoriteCreateRequest;
import nextstep.member.domain.Member;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_FORMAT = "Bearer %s";

    public static String 로그인요청(Member member) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(member.getEmail(), member.getPassword()))
                .when().post("/login/token")
                .then().log().all().extract()
                .jsonPath().getObject("accessToken", String.class);
    }

    public static ExtractableResponse<Response> 즐겨찾기_등록(String accessToken, Long source, Long target) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION_HEADER, String.format(BEARER_FORMAT, accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteCreateRequest(source, target))
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION_HEADER, String.format(BEARER_FORMAT, accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static Long 즐겨찾기_ID_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION_HEADER, String.format(BEARER_FORMAT, accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract()
                .jsonPath().getObject("[0].id", Long.class);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String accessToken, Long id) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION_HEADER, String.format(BEARER_FORMAT, accessToken))
                .pathParam("favoriteId", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{favoriteId}")
                .then().log().all().extract();
    }

}
