package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.DataLoader.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthSteps {
    public static RequestSpecification givenAdminRole() {
        String adminToken = 로그인_되어_있음(ADMIN_EMAIL, PASSWORD);

        return RestAssured.given().log().all()
                .auth().oauth2(adminToken);
    }

    public static RequestSpecification givenUserRole() {
        String userToken = 로그인_되어_있음(MEMBER_EMAIL, PASSWORD);

        return RestAssured.given().log().all()
                .auth().oauth2(userToken);
    }

    public static String 로그인_되어_있음(String email, String password) {
        return 로그인_요청(email, password).jsonPath().getString("accessToken");
    }

    private static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 권한검사에_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
