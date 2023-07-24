package subway.acceptance.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;
import subway.utils.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder().email(EMAIL).password(PASSWORD).age(AGE).build();
        memberRepository.save(member);
    }


    /**
     * Given 아이디와 패스워드가 있고
     * When 로그인을 하면
     * Then 토큰이 발급된다.
     */
    @DisplayName("로그인에 성공하면 토큰이 발급된다")
    @Test
    void getTokenWithSuccessLogin() {
        // given
        var 로그인_요청 = AuthFixture.로그인_요청_만들기(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(로그인_요청)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When 비밀번호가 잘못 되었을 때
     * Then 로그인이 되지 않는다
     */
    @DisplayName("비밀번호가 틀리면 로그인이 되지 않는다")
    @Test
    void failLoginWithInvalidPassword() {
        // when
        var 로그인 = AuthFixture.로그인_요청_만들기(EMAIL, "FAILED_PASSWORD");
        var 로그인_응답 = AuthSteps.로그인_API(로그인);

        // then
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * When 없는 아이디로 로그인을 할 때
     * Then 로그인이 되지 않는다
     */
    @DisplayName("아이디가 틀리면 로그인이 되지 않는다")
    @Test
    void failLoginWithInvalidLoginEmail() {
        // when
        var 로그인 = AuthFixture.로그인_요청_만들기("FAKE_EMAIL", PASSWORD);
        var 로그인_응답 = AuthSteps.로그인_API(로그인);

        // then
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * When 위조 토큰으로 인가를 시도하면
     * Then 인가가 되지 않는다.
     */
    @DisplayName("위조 토큰으로 시도하는 인가는 실패한다")
    @Test
    void validTokenWithInvalidToken() {
        // when
        var response = RestAssured.given().log().all()
                .header("Authorization", AuthFixture.BEARER_만들기("asdf.asdf.asdf"))
                .when().get("/members/me")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
