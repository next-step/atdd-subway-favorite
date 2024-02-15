package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static final String FAVORITES_URL = "/favorites";

    private FavoriteSteps() {

    }

    public static ExtractableResponse<Response> 즐겨찾기를_등록한다(final Long source, final Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));
        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITES_URL)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

}
