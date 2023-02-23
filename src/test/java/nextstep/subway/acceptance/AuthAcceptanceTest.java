package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.fake.GithubResponses;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static nextstep.subway.acceptance.MemberSteps.깃헙_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    @Autowired
    private MemberService memberService;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, 20);
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth 성공")
    @Test
    void githubAuth() {
        회원_생성_요청(GithubResponses.사용자1.getEmail(), PASSWORD, 20);
        ExtractableResponse<Response> response = 깃헙_인증_로그인_요청(GithubResponses.사용자1.getCode());

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth 예외 (없는 계정 요청)")
    @Test
    void githubAuthRedirect() {
        ExtractableResponse<Response> response = 깃헙_인증_로그인_요청(GithubResponses.사용자1.getCode());

        MemberResponse memberResponse = memberService.findMine(GithubResponses.사용자1.getEmail());

        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                  () -> assertThat(response.jsonPath().getString("accessToken")).isNotBlank(),
                  () -> assertThat(null != memberResponse).isTrue());
    }
}