package nextstep.auth.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.fake.GithubResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Auth 관련 인수 테스트")
class AuthControllerTest extends AcceptanceTest {

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new HashMap<String, String>(){{
                    put("code",GithubResponse.사용자2.getCode());
                }})
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        assertThat(response.jsonPath().getString("accessToken")).isEqualTo(GithubResponse.사용자2.getAccessToken());


    }

}
