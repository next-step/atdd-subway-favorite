package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.GithubClient;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.unit.GithubSampleResponse;
import nextstep.subway.unit.StubGithubClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthAcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Bearer Auth Exception")
    @Test
    void bearerAuthException() {
        베어러_인증_패스워드_예외발생(EMAIL, "no" + PASSWORD);
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubSampleResponse.사용자1.getCode());

        // when
        ExtractableResponse<Response> response = 깃허브_로그인_요청(params);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        GithubClient stubGithubClient() {
            return new StubGithubClient();
        }
    }
}