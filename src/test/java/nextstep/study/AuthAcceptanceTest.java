package nextstep.study;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        Map<String, String> params = new HashMap<>();
        params.put("code", "code");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * Given {code, cliend_id, client_secret} json body 값과 함께
     * When Accept: application/json 형식으로
     * And POST 요청을 https://github.com/login/oauth/access_token에 보내면
     * Then access_token, scope, token_type이 json으로 body에 담겨 응답이 온다.
     * Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
     * */

    /**
     * Given POST 요청 https://github.com/login/oauth/access_token으로 깃허브로 부터 받은 accessToken이 있을 때
     * When GET 요청을 https://api.github.com/user에 아래 헤더와 함께 보내면,
     *   -H "Accept: application/vnd.github+json" \
     *   -H "Authorization: Bearer <YOUR-TOKEN>" \
     *   -H "X-GitHub-Api-Version: 2022-11-28"
     * Then email 값이 json으로 body에 담겨 응답이 온다.
     * Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
     * */
}