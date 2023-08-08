package nextstep.study;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.utils.GithubResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthSteps {
  public static String 로그인_후_엑세스토큰_획득(String email, String password) {
    Map<String, String> params = new HashMap<>();
    params.put("email", email);
    params.put("password", password);

    ExtractableResponse<Response> response = RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(params)
        .when().post("/login/token")
        .then().log().all()
        .statusCode(HttpStatus.OK.value()).extract();
    return response.jsonPath().getString("accessToken");
  }

  public static ExtractableResponse<Response> 깃헙_로그인_응답값_반환(GithubResponses responses) {
    Map<String, String> params = new HashMap<>();
    params.put("code", responses.getCode());

    return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/github")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();
  }
}
