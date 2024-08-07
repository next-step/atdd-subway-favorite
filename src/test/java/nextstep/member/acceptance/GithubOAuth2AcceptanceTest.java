package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.ui.AuthenticationPrincipal;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.GithubOauth2ApiRequest.로그인_한다;
import static nextstep.member.acceptance.MemberApiRequest.내_정보를_조회한다;
import static nextstep.member.application.FakeGithubOAuth2Client.GithubResponses;
import static org.assertj.core.api.Assertions.assertThat;

class GithubOAuth2AcceptanceTest extends AcceptanceTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("oauth2 인가된 email과 일치하는 유저가 존재하면 accessToken을 바로 반환한다")
    @Test
    void loginTest1() {
        //Given 회원이 이미 존재할 때
        var 회원1 = GithubResponses.사용자1;
        memberRepository.save(new Member(회원1.getEmail(), "password", 19));

        //When oauth2 로그인시
        var accessToken = 로그인_한다(회원1.getCode()).jsonPath().getString("accessToken");

        //Then accessToken 으로 로그인 할 수 있다.
        Response 내_정보 = 내_정보를_조회한다(accessToken);
        assertThat(내_정보.jsonPath().getString("email")).isEqualTo(회원1.getEmail());
    }

    @DisplayName("oauth2 인가된 email과 일치하는 유저가 없으면 회원가입 후 accessToken을 바로 반환한다")
    @Test
    void loginTest2() {
        //Given 가입되지 않은 유저가
        //When oauth2 로그인을 하면
        var 회원1 = GithubResponses.사용자1;
        var accessToken = 로그인_한다(회원1.getCode()).jsonPath().getString("accessToken");

        //Then 내정보 조회시 생성된 계정정보를 확인할 수 있다
        Response response2 = 내_정보를_조회한다(accessToken);
        assertThat(response2.jsonPath().getString("email")).isEqualTo(회원1.getEmail());
    }
}
