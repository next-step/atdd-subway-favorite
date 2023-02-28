package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.exception.ErrorMessage;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.unit.GithubSampleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.깃허브_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.응답_상태코드_검증;
import static nextstep.subway.acceptance.MemberSteps.응답_예외_메시지_검증;
import static nextstep.subway.acceptance.MemberSteps.토큰으로_회원_정보_조회;
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
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, "no" + PASSWORD);

        // then
        응답_상태코드_검증(response, ErrorMessage.MEMBER_PASSWORD_NOT_EQUAL.getHttpStatus());
        응답_예외_메시지_검증(response, ErrorMessage.MEMBER_PASSWORD_NOT_EQUAL);
    }

    @DisplayName("Github 로그인 후 회원가입이 되어있지않다면 회원가입 진행 후 토큰 발행")
    @Test
    void signUpByGithubIfNotExistMember() {
        // given
        GithubSampleResponse 사용자 = GithubSampleResponse.사용자1;
        Map<String, String> params = new HashMap<>();
        params.put("code", 사용자.getCode());

        // when -> 토큰 만료 예외 (테스트용 stub처리 필요?)
        ExtractableResponse<Response> response = 깃허브_로그인_요청(params);
        String accessToken = response.jsonPath().getString("accessToken");
        ExtractableResponse<Response> memberInfoResponse = 토큰으로_회원_정보_조회(accessToken);

        // then
        assertThat(accessToken).isNotBlank();
        assertThat(memberInfoResponse.jsonPath().getString("email")).isEqualTo(사용자.getEmail());
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
}