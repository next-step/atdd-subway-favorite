package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import javax.print.attribute.standard.Media;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import org.springframework.http.MediaType;

public class FavoritesSteps {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, FavoriteRequest param) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

}
