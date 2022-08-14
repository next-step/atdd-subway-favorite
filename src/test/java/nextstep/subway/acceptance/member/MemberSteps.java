package nextstep.subway.acceptance.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.GivenUtils.ACCESS_TOKEN;
import static nextstep.subway.utils.GivenUtils.AGE;
import static nextstep.subway.utils.GivenUtils.EMAIL;
import static nextstep.subway.utils.GivenUtils.ID;
import static nextstep.subway.utils.GivenUtils.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberSteps {

    private static final String LOGIN_TOKEN_PATH = "/login/token";
    private static final String MEMBERS_PATH = "/members";
    private static final String LOCATION_HEADER = "Location";

    public static String 로그인_되어_있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        return response.jsonPath().getString(ACCESS_TOKEN);
    }

    public static String 어드민_로그인_되어_있음() {
        return 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, email);
        params.put(PASSWORD, password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(LOGIN_TOKEN_PATH)
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, email);
        params.put(PASSWORD, password);
        params.put(AGE, String.valueOf(age));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(MEMBERS_PATH)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION_HEADER);

        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header(LOCATION_HEADER);

        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, email);
        params.put(PASSWORD, password);
        params.put(AGE, String.valueOf(age));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION_HEADER);
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all().extract();
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.jsonPath().getString(ID)).isNotNull();
        assertThat(response.jsonPath().getString(EMAIL)).isEqualTo(email);
        assertThat(response.jsonPath().getInt(AGE)).isEqualTo(age);
    }
}