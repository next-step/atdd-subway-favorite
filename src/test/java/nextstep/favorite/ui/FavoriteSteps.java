package nextstep.favorite.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.CreateFavoriteRequest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static CreateFavoriteRequest requestDto(Long source, Long target) {
        return new CreateFavoriteRequest(source, target);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, CreateFavoriteRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }
}
