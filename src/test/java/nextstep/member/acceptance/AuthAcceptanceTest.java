package nextstep.member.acceptance;

import static nextstep.member.acceptance.AuthSteps.깃허브_로그인_요청_하기;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.test.GithubResponses;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

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

        ExtractableResponse<Response> response = AuthSteps.인증_요청_하기(EMAIL, PASSWORD);

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        response = MemberSteps.회원_정보_조회_요청(accessToken);

        assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * Given code 가 주어진다.
     * When github 로그인을 시도한다.
     * Then 회원가입이 완료되고, 로그인이 성공한다.
     */
    @Test
    @DisplayName("Github 로그인")
    void githubLogin() {
        // given
        GithubResponses 사용자2 = GithubResponses.사용자2;
        ExtractableResponse<Response> response = AuthSteps.인증_요청_하기(사용자2.getEmail(), "password");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("존재하지 않는 회원입니다.");

        String code = 사용자2.getCode();
        // when
        response = 깃허브_로그인_요청_하기(code);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();

    }

    /**
     * Given 이미 깃허브로 로그인 한 사용자가 주어진다.
     * When github 로그인을 시도한다.
     * Then Github 로 이미 가입된 회원이라도 로그인이 성공한다.
     */
    @Test
    @DisplayName("이미 가입된 회원의 Github 로그인")
    void alreadyJoinedGithubLogin() {
        // given
        GithubResponses 이미_가입된_사용자 = GithubResponses.이미_가입된_사용자;

        ExtractableResponse<Response> response = 깃허브_로그인_요청_하기(이미_가입된_사용자.getCode());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        response = MemberSteps.회원_정보_조회_요청(response.jsonPath().getString("accessToken"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        String code = 이미_가입된_사용자.getCode();
        // when
        response = 깃허브_로그인_요청_하기(code);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
