package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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

    @DisplayName("Github Member Auth")
    @ParameterizedTest
    @EnumSource(GithubResponse.class)
    void githubMemberAuth(GithubResponse githubResponse) {
        ExtractableResponse<Response> response = 깃헙_로그인_요청(githubResponse.getCode());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github NonMember Auth")
    @Test
    void githubNonMemberAuth() {
        ExtractableResponse<Response> response = 깃헙_로그인_요청(NON_MEMBER_CODE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}

