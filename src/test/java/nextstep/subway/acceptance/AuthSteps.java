package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원 정보 조회, 입력, 수정과 관련 없은
 * 순수 회원의 인증과 관련된 행위를 모은다.
 */
public class AuthSteps {

    public static ExtractableResponse<Response> 베어러_인증_로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    // 깃허브 인증은 회원정보를 이용해 인증을 처리하고 인증 여부를 확인하는 엑섹서 토큰을 전달받으므로 관심사가 회원정보가 아니므로 분리한다
    public static ExtractableResponse<Response> 깃허브_인증_로그인_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/github")
                .then().log().all()
                .extract();
    }
}