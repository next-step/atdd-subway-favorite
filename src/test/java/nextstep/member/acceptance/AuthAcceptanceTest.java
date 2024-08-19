package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.GithubResponses;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.AuthSteps.깃헙_로그인_요청;
import static nextstep.member.acceptance.AuthSteps.유저_정보_조회;
import static nextstep.member.acceptance.AuthSteps.토큰_획득;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when
        ExtractableResponse<Response> reponseToken = 토큰_획득(EMAIL, PASSWORD);

        // then
        String accessToken = reponseToken.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response = 유저_정보_조회(accessToken);
        assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    @DisplayName("Github Auth 회원가입 진행 후 토큰 발행")
    @Test
    void githubAuth_회원가입_진행_후_토큰_발행() {
        // when
        ExtractableResponse<Response> response = 깃헙_로그인_요청(GithubResponses.사용자3.getCode());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Github Auth 기존유저 토큰 발행")
    @Test
    void githubAuth_기존유저_토큰_발행() {
        // given
        memberRepository.save(new Member(GithubResponses.사용자1.getEmail(), null, null));

        // when
        ExtractableResponse<Response> response = 깃헙_로그인_요청(GithubResponses.사용자1.getCode());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}