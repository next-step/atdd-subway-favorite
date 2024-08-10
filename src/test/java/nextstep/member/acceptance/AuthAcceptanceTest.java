package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static nextstep.member.acceptance.AuthSteps.Github_로그인_토큰_요청;
import static nextstep.member.acceptance.AuthSteps.로그인_토큰_요청;
import static nextstep.member.acceptance.MemberSteps.본인_정보_조회;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 토큰 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthAcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Bearer Auth")
    void bearerAuth() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        ExtractableResponse<Response> response = 로그인_토큰_요청(EMAIL, PASSWORD);
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response2 = 본인_정보_조회(accessToken);

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName("Github Auth")
    void githubAuth() {
        ExtractableResponse<Response> response = Github_로그인_토큰_요청("code1");

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
