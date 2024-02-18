package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.AuthSteps.*;
import static nextstep.member.acceptance.MemberSteps.*;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.utils.GithubResponses.사용자1;
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

        ExtractableResponse<Response> response = 이메일_패스워드로_로그인을_요청한다(EMAIL, PASSWORD);

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

    /**
     * When 회원이 등록되어 있다.
     * And code로 깃헙을 통한 로그인 API를 요청한다.
     * Then 200 코드를 리턴한다.
     * And accessToken을 리턴한다.
     */
    @DisplayName("회원가입을 한 사용자가 code를 통한 Github Login")
    @Test
    void githubLogin_registerMember() {
        회원_생성_요청(사용자1.email(), PASSWORD, AGE);
        final ExtractableResponse<Response> response = 코드로_깃허브를_통한_로그인을_요청한다(사용자1.code());

        HTTP코드를_검증한다(response, HttpStatus.OK);
        토큰을_응답한다(response, createToken(사용자1.email()));
    }

    /**
     * When code로 깃헙을 통한 로그인 API를 요청한다.
     * Then 200 코드를 리턴한다.
     * And accessToken을 리턴한다.
     * And 회원가입이 완료된다
     */
    @DisplayName("회원가입하지 않은 사용자가 code를 통한 Github Login 요청")
    @Test
    void githubLogin_unRegisterMember() {
        final String accessToken = createToken(사용자1.email());
        final ExtractableResponse<Response> response = 코드로_깃허브를_통한_로그인을_요청한다(사용자1.code());

        HTTP코드를_검증한다(response, HttpStatus.OK);
        토큰을_응답한다(response, accessToken);
        토큰으로_회원_정보_조회_요청(accessToken);
    }

    private static void HTTP코드를_검증한다(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
    private static void 토큰을_응답한다(ExtractableResponse<Response> response, String accessToken) {
        assertThat(response.as(TokenResponse.class).getAccessToken()).isEqualTo(accessToken);
    }
}