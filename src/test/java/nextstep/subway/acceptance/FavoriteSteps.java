package nextstep.subway.acceptance;

import static nextstep.study.AuthSteps.깃헙_로그인;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static void 즐겨_찾기_생성_검증(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).isNotNull()
        );
    }

    public static ExtractableResponse<Response> 깃헙_AccessToken으로_즐겨_찾기_생성한다(String accessToken) {
        Map<String, String> body = new HashMap<>();
        body.put("source", "1");
        body.put("target", "3");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(body).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static String 깃헙_로그인하고_AccessToken_받아온다() {
        var response = 깃헙_로그인("aofijeowifjaoief");
        return response.jsonPath().getString("accessToken");
    }

}
