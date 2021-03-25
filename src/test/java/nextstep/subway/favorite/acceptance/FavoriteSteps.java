package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {
    public static Map<String, String> 즐겨찾기_파라미터_설정(String stationId, String targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("sourceId", stationId);
        params.put("targetId", targetId);
        return params;
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Map<String, String> params, TokenResponse 정상토큰) {
        return RestAssured.given().log().all(true)
                .auth().oauth2(정상토큰.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(params)
                .post("/favorites")
                .then().log().all(true)
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse 정상토큰) {
        return RestAssured.given().log().all(true)
                .auth().oauth2(정상토큰.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/favorites")
                .then().log().all(true)
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createResponse, TokenResponse 정상토큰) {
        return RestAssured.given().log().all(true)
                .auth().oauth2(정상토큰.getAccessToken())
                .when()
                .delete(createResponse.header("Location"))
                .then().log().all(true)
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> readAllResponse) {
        assertThat(readAllResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 인증_실패함(ExtractableResponse<Response> invalidCreateResponse) {
        assertThat(invalidCreateResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
