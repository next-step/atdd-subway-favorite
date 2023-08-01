package nextstep.study;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.dto.TokenRequest;
import nextstep.auth.oauth2.github.dto.GithubTokenRequest;
import nextstep.member.repository.MemberRepository;
import nextstep.support.AcceptanceTest;
import nextstep.support.DatabaseCleanup;
import nextstep.support.RestAssuredClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static nextstep.member.fixture.MemberFixture.회원_정보_엔티티;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
class AuthAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanUp;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleanUp.execute();

        memberRepository.save(회원_정보_엔티티);
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
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