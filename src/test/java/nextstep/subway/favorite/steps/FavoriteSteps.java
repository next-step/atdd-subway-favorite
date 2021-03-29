package nextstep.subway.favorite.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/favorites")
                .then().log().all().extract();
    }

    public static Long 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return Long.parseLong(createResponse.header("Location").split("/")[2]);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/favorites")
                .then().log().all().extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, Long createdId) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("id")).isEqualTo(createdId);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청됨(TokenResponse tokenResponse, Long createdId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when()
                .delete("/favorites/" + createdId)
                .then().log().all().extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
