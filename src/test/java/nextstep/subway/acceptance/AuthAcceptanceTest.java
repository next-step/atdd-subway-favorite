package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.acceptance.MemberSteps.깃헙_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final String NON_MEMBER_CODE = "abc";

    @Autowired
    private DataLoader dataLoader;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        dataLoader.loadData();
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Member Github Auth")
    @ParameterizedTest
    @ValueSource(strings = {"832ovnq039hfjn", "mkfo0aFa03m", "m-a3hnfnoew92", "nvci383mciq0oq"})
    void memberGithubAuth(String memberCode) {
        ExtractableResponse<Response> response = 깃헙_로그인_요청(memberCode);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("NonMember Github Auth")
    @Test
    void nonMemberGithubAuth() {
        ExtractableResponse<Response> response = 깃헙_로그인_요청(NON_MEMBER_CODE);

        // 400에러가 발생한 경우에는 회원가입을 진행해야 한다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}

