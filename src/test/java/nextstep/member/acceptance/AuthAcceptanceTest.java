package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.unit.GithubUser;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("토큰 인증 방식 테스트")
class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Given: 일반 회원으로 가입한 유저가 있고
     * When: 이메일과 비밀번호를 통해 로그인하면
     * Then: 해당 회원에 대한 액세스 토큰이 발급되어 회원 정보를 조회할 수 있다.
     */
    @Test
    void 기본_로그인_인증() {
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
     * Given: 회원가입을 하지 않은 유저가 있고
     * When: 깃허브 로그인을 하는 경우
     * Then: 깃허브 로그인을 통해 전달 받은 회원 정보로 회원 가입을 수행하고, 액세스 토큰을 발급한다.
     */
    @Test
    void 깃허브_로그인_인증() {
        GithubUser 사용자1 = GithubUser.사용자1;
        Map<String, String> params = new HashMap<>();
        params.put("code", 사용자1.getCode());

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
