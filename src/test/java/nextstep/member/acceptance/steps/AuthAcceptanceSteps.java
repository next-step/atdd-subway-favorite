package nextstep.member.acceptance.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
}
