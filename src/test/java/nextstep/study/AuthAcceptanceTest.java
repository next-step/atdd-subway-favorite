package nextstep.study;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.GithubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.study.LoginSteps.code로_깃허브_로그인;
import static nextstep.study.LoginSteps.비밀번호로_로그인;
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

        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when
        ExtractableResponse<Response> response = 비밀번호로_로그인(EMAIL, PASSWORD);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth")
    @EnumSource(GithubResponse.class)
    @ParameterizedTest
    void githubAuth(GithubResponse githubResponse) {

        // when
        ExtractableResponse<Response> response = code로_깃허브_로그인(githubResponse.getCode());

        // then
        String accessToken = response.jsonPath().getString("accessToken");
        ExtractableResponse<Response> profileResponse = MemberSteps.토큰으로_회원_정보_조회_요청(accessToken);
        assertThat(profileResponse.jsonPath().getString("email")).isEqualTo(githubResponse.getEmail());
        assertThat(profileResponse.jsonPath().getInt("age")).isEqualTo(githubResponse.getAge());

    }

    @DisplayName("Github Auth - fail - wrong code")
    @Test
    void githubAuth_wrongCode() {

        // given
        String wrongCode = "wrongCode";

        // when
        ExtractableResponse<Response> response = code로_깃허브_로그인(wrongCode);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }
}