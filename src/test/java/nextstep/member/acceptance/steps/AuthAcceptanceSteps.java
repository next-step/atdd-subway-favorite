package nextstep.member.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class AuthAcceptanceSteps {
  private AuthAcceptanceSteps() {}

  public static String 로그인_요청(String email, String password) {
    Map<String, String> params = new HashMap<>();
    params.put("email", email);
    params.put("password", password);
    ExtractableResponse<Response> response =
        RestAssured.given()
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .post("/login/token")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    return response.jsonPath().getString("accessToken");
  }

  public static ExtractableResponse<Response> 잘못된_정보로_로그인_요청(String email, String password) {
    Map<String, String> params = new HashMap<>();
    params.put("email", email);
    params.put("password", password);
    return RestAssured.given()
        .log()
        .all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(params)
        .when()
        .post("/login/token")
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 인증_실패함(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }
}
