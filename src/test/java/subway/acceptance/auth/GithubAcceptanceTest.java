package subway.acceptance.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.utils.AcceptanceTest;
import subway.utils.GithubResponses;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GitHubClient 관련 인수 테스트")
public class GithubAcceptanceTest extends AcceptanceTest {

    /**
     * Given code가 있고
     * When GitHub 인증을 하면
     * Then JWT 토큰을 반환한다.
     */
    @DisplayName("code로 GitHub 인증을 할 수 있다")
    @Test
    void githubAuth() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponses.사용자1.getCode());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
