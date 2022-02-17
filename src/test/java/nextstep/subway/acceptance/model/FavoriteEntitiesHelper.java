package nextstep.subway.acceptance.model;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.acceptance.model.MemberEntitiesHelper.로그인_되어_있음;
import static nextstep.auth.acceptance.model.MemberEntitiesHelper.회원가입을_한다;
import static nextstep.subway.acceptance.model.LineEntitiesHelper.newLine;
import static nextstep.subway.acceptance.model.StationEntitiesHelper.강남역;
import static nextstep.subway.acceptance.model.StationEntitiesHelper.역삼역;
import static nextstep.subway.acceptance.model.StationEntitiesHelper.지하철역_생성_요청후_아이디_조회;
import static org.apache.http.HttpHeaders.LOCATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public final class FavoriteEntitiesHelper {

    private static final String REQUEST_URI = "/favorites";
    public static Long 역삼역_ID;
    public static Long 강남역_ID;
    public static Map<String, Object> 이호선 = new HashMap<>();
    public static String accessToken;

    public static void givens() {
        역삼역_ID = 지하철역_생성_요청후_아이디_조회(역삼역);
        강남역_ID = 지하철역_생성_요청후_아이디_조회(강남역);
        이호선 = newLine("이호선", "bg-green-600", 강남역_ID, 역삼역_ID, 2);
        회원가입을_한다();
        accessToken = 로그인_되어_있음();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(newFavorite(source, target))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(REQUEST_URI)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header(LOCATION)).isNotBlank();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get(REQUEST_URI)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(response.jsonPath().getList("source")).hasSize(1);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String uri) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    private static Map<String, Long> newFavorite(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return params;
    }

    public static void 권한이_존재하지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }
}
