package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse 즐겨찾기_만들기(TokenResponse tokenResponse, FavoriteRequest favoriteRequest){
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse 즐겨찾기_확인(TokenResponse tokenResponse){
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse 즐겨찾기_삭제(TokenResponse tokenResponse, ExtractableResponse<Response> createResponse){
        String uri = createResponse.header("Location");
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(uri)
                .then().log().all().extract();
    }
}
