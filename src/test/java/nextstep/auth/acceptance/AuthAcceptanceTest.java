package nextstep.auth.acceptance;

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

    @DisplayName("로그인 요청 시 로그인에 성공하면 토큰을 응답한다.")
    @Test
    void 로그인요청() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when
        ExtractableResponse<Response> response = AuthSteps.로그인요청(EMAIL, PASSWORD);

        // then
        로그인요청_응답값_검증(response);
    }

    @DisplayName("로그인 요청 시 패스워드 검증에 실패하면 실패응답한다.")
    @Test
    void 로그인요청_패스워드검증실패() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when
        ExtractableResponse<Response> response = AuthSteps.로그인요청(EMAIL, "");

        // then
        로그인요청_패스워드검증실패_응답값_검증(response);
    }

    @DisplayName("로그인 요청 시 패스워드 검증에 실패하면 실패응답한다.")
    @Test
    void 로그인요청_사용자미존재() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when
        ExtractableResponse<Response> response = AuthSteps.로그인요청("", "");

        // then
        로그인요청_사용자미존재_응답값_검증(response);
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

    private void 로그인요청_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getObject("accessToken", String.class)).isNotBlank();
    }

    private void 로그인요청_패스워드검증실패_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.asString()).isEqualTo("인증에 실패했습니다.");
    }

    private void 로그인요청_사용자미존재_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.asString()).isEqualTo("인증에 실패했습니다.");
    }

}