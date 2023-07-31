package nextstep.study;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.oauth2.github.GithubTokenRequest;
import nextstep.member.domain.MemberRepository;
import nextstep.support.AcceptanceTest;
import nextstep.support.RestAssuredClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.member.fixture.MemberFixture.회원_정보_엔티티;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
class AuthAcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        memberRepository.save(회원_정보_엔티티);

        TokenRequest 토큰_요청 = TokenRequest.builder()
                .email(회원_정보_엔티티.getEmail())
                .password(회원_정보_엔티티.getPassword())
                .build();

        ExtractableResponse<Response> 토큰_응답 = RestAssuredClient.post("/login/token", 토큰_요청);

        assertThat(토큰_응답.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        GithubTokenRequest 깃허브_토큰_요청 = new GithubTokenRequest("code");

        ExtractableResponse<Response> 깃허브_토큰_응답 = RestAssuredClient.post("/login/github", 깃허브_토큰_요청);

        assertThat(깃허브_토큰_응답.jsonPath().getString("accessToken")).isNotBlank();
    }
}