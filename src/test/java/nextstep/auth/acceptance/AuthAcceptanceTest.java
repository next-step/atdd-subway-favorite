package nextstep.auth.acceptance;

import static nextstep.auth.acceptance.AuthSteps.깃허브_로그인_요청;
import static nextstep.auth.acceptance.AuthSteps.로그인_후_토큰_반환;
import static nextstep.member.acceptance.MemberSteps.회원_프로필_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.auth.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        //given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        //when
        String accessToken = 로그인_후_토큰_반환(EMAIL, PASSWORD);

        //then
        assertThat(accessToken).isNotBlank();

        //then
        ExtractableResponse<Response> 회원_프로필_조회_요청_응답 = 회원_프로필_조회_요청(accessToken);
        assertThat(회원_프로필_조회_요청_응답.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        //given
        GithubResponses 사용자1 = GithubResponses.사용자1;

        //when
        ExtractableResponse<Response> 로그인_요청_응답 = 깃허브_로그인_요청(사용자1.getCode(), 사용자1.getAccessToken());
        String accessToken = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();

        //then
        assertThat(accessToken).isNotBlank();
        assertThat(accessToken).isEqualTo(사용자1.getAccessToken());
    }
}
