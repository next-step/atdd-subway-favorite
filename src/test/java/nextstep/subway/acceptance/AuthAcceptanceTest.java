package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import nextstep.member.application.dto.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        super.setUp();
        dataLoader.loadData();
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When Github 로그인 요청 시
     * Then accessToken 을 얻을 수 있다.
     */
    @DisplayName("Github 로그인 요청 시 accessToken 을 얻을 수 있다")
    @Test
    void Github_로그인_요청_시_accessToken_을_얻을_수_있다() {
        ExtractableResponse<Response> response = 깃헙_로그인_요청(GithubResponses.사용자1.getCode());

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        assertThat(response.jsonPath().getString("accessToken")).isEqualTo(GithubResponses.사용자1.getAccessToken());
    }

    private static ExtractableResponse<Response> 깃헙_로그인_요청(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/github")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }

}
