package nextstep.member.acceptance;

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

import static nextstep.member.acceptance.AuthSteps.로그인을_요청한다;
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

        ExtractableResponse<Response> response = 로그인을_요청한다(EMAIL, PASSWORD);

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response2 = 개인정보_요청(accessToken);

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * When JWT 토큰 없이 인증이 필요한 API를 요청한다.
     * Then 401 코드를 리턴한다.
     */
    @DisplayName("Jwt토큰이 없으면 401코드를 리턴한다.")
    @Test
    void bearerAuth_empty_jwt() {
        final ExtractableResponse<Response> response = JWT없이_개인정보_요청();

        HTTP코드를_검증한다(response, HttpStatus.UNAUTHORIZED);
    }

    private static void HTTP코드를_검증한다(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private static ExtractableResponse<Response> 개인정보_요청(final String accessToken) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
        return response;
    }

    private static ExtractableResponse<Response> JWT없이_개인정보_요청() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/members/me")
                .then().log().all()
                .extract();
        return response;
    }
}