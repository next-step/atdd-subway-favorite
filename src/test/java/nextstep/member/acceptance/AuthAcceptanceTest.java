package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import nextstep.common.AcceptanceTest;
import nextstep.member.utils.fixture.GithubAuthFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.utils.steps.MemberSteps.회원_삭제_요청;
import static nextstep.member.utils.steps.MemberSteps.회원_생성_요청;
import static nextstep.member.utils.steps.TokenSteps.깃헙_로그인_요청;
import static nextstep.member.utils.steps.TokenSteps.인증정보_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * Scenario: 이미 가입된 회원으로 깃헙 로그인 성공
     * Given 회원가입으로 회원을 생성
     * When 깃헙 로그인 요청 시
     * Then accessToken이 올바르게 생성된다.
     */
    @DisplayName("가입된 회원 깃헙 로그인 요청")
    @Test
    void 가입됭_회원_깃헙_로그인() {
        // given
        GithubAuthFixture 회원 = GithubAuthFixture.사용자1;
        회원_생성_요청(회원.getEmail(), 회원.getPassword(), 10);

        // when
        ExtractableResponse<Response> response = 깃헙_로그인_요청(회원.getCode());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String accessToken = 인증정보_생성_요청(회원.getEmail(), 회원.getPassword()).jsonPath().getString("accessToken");
        assertThat(response.jsonPath().getString("accessToken")).isEqualTo(accessToken);
    }

    /**
     * Scenario: 가입 안된 회원으로 깃헙 로그인 성공
     * Given 존재하지 않는 회원으로
     * When 깃헙 로그인 요청 시
     * Then accessToken을 확인할 수 있다.
     */
    @DisplayName("미가입 회원 깃헙 로그인 요청")
    @Test
    void 미가입_회원_깃헙_로그인() {
        // given
        GithubAuthFixture 회원 = GithubAuthFixture.사용자2;
        var createResponse = 회원_생성_요청(회원.getEmail(), 회원.getPassword(), 10);
        회원_삭제_요청(createResponse);

        // when
        ExtractableResponse<Response> response = 깃헙_로그인_요청(회원.getCode());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String accessToken = 인증정보_생성_요청(회원.getEmail(), 회원.getPassword()).jsonPath().getString("accessToken");
        assertThat(response.jsonPath().getString("accessToken")).isEqualTo(accessToken);
    }
}
