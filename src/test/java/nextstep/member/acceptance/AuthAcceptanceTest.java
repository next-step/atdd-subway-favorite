package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static nextstep.utils.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("토큰 인증 관련 인수 테스트")
@ActiveProfiles("test")
class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this);
        memberRepository.save(new Member(사용자1.getEmail(), 사용자1.getPassword(), 사용자1.getAge()));
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("email", 사용자1.getEmail());
        params.put("password", 사용자1.getPassword());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        // then
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        // when
        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        // then
        assertThat(response2.jsonPath().getString("email")).isEqualTo(사용자1.getEmail());
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", 사용자1.getCode());

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