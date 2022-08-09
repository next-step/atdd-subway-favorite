package nextstep.subway.acceptance.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.auth.AuthSteps.베어러_인증으로_회원_정보_조회;
import static nextstep.subway.acceptance.auth.AuthSteps.베이직_인증으로_회원_정보_조회;
import static nextstep.subway.acceptance.auth.AuthSteps.폼_로그인_후_회원_정보_조회;
import static nextstep.subway.acceptance.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.member.MemberSteps.회원_정보_조회됨;
import static nextstep.subway.utils.GivenUtils.ADMIN_AGE;
import static nextstep.subway.utils.GivenUtils.ADMIN_EMAIL;
import static nextstep.subway.utils.GivenUtils.ADMIN_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("베이직 인증으로 내 정보 조회")
    void basicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_회원_정보_조회(ADMIN_EMAIL, ADMIN_PASSWORD);

        회원_정보_조회됨(response, ADMIN_EMAIL, ADMIN_AGE);
    }

    @Test
    @DisplayName("베이직 인증으로 내 정보 조회 실패")
    void invalidBasicAuth() {
        String invalidEmail = "xxx@nextstep.com";

        ExtractableResponse<Response> response = 베이직_인증으로_회원_정보_조회(invalidEmail, ADMIN_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("폼 로그인 후 내 정보 조회")
    void sessionAuth() {
        ExtractableResponse<Response> response = 폼_로그인_후_회원_정보_조회(ADMIN_EMAIL, ADMIN_PASSWORD);

        회원_정보_조회됨(response, ADMIN_EMAIL, ADMIN_AGE);
    }

    @Test
    @DisplayName("폼 로그인 후 내 정보 조회 실패")
    void invalidSessionAuth() {
        String invalidEmail = "xxx@nextstep.com";

        ExtractableResponse<Response> response = 폼_로그인_후_회원_정보_조회(invalidEmail, ADMIN_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Bearer 인증으로 내 정보 조회")
    void bearerAuth() {
        String accessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);

        ExtractableResponse<Response> response = 베어러_인증으로_회원_정보_조회(accessToken);

        회원_정보_조회됨(response, ADMIN_EMAIL, ADMIN_AGE);
    }

    @Test
    @DisplayName("Bearer 인증으로 내 정보 조회 실패")
    void invalidBearerAuth() {
        String invalidToken = "xxx";

        ExtractableResponse<Response> response = 베어러_인증으로_회원_정보_조회(invalidToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
