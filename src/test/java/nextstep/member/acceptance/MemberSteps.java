package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.common.CommonSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberSteps {

    private static final String BASE_URL = "/members";

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(BASE_URL)
                .then().log().all().extract();
    }

    public static String 회원_생성_요청_성공(String email, String password, Integer age) {
        ExtractableResponse<Response> response = 회원_생성_요청(email, password, age);
        return response.header("Location");
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(String memberUrl) {

        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(memberUrl)
                .then().log().all()
                .extract();
    }

    public static MemberResponse 회원_정보_조회_요청_성공(String memberUrl) {
        ExtractableResponse<Response> response = 회원_정보_조회_요청(memberUrl);
        checkHttpResponseCode(response, HttpStatus.OK);
        return response.jsonPath().getObject("", MemberResponse.class);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(String memberUrl, String email, String password, Integer age) {

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(memberUrl)
                .then().log().all().extract();
    }

    public static void 회원_정보_수정_요청_성공(String memberUrl, String email, String password, Integer age) {
        ExtractableResponse<Response> response = 회원_정보_수정_요청(memberUrl, email, password, age);
        checkHttpResponseCode(response, HttpStatus.OK);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(String memberUrl) {
        return RestAssured
                .given().log().all()
                .when().delete(memberUrl)
                .then().log().all().extract();
    }

    public static void 회원_삭제_요청_성공(String memberUrl) {
        ExtractableResponse<Response> response = 회원_삭제_요청(memberUrl);
        checkHttpResponseCode(response, HttpStatus.NO_CONTENT);
    }

    public static ExtractableResponse<Response> 토큰_기반_로그인_요청(String email, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .when().get(BASE_URL + "/me")
                .then().log().all()
                .extract();
    }

    public static MemberResponse 내_정보_조회_요청_성공(String accessToken) {
        ExtractableResponse<Response> response = 내_정보_조회_요청(accessToken);
        checkHttpResponseCode(response, HttpStatus.OK);
        return response.jsonPath().getObject("", MemberResponse.class);
    }
}