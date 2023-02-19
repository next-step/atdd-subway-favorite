package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.JWT_토큰으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.응답에서_email_정보_확인;
import static nextstep.subway.acceptance.MemberSteps.응답에서_id_정보_있는지_확인;
import static nextstep.subway.acceptance.MemberSteps.응답에서_나이_정보_확인;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        MemberSteps.응답에서_access_token_존재_여부_확인(response);
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        ExtractableResponse<Response> response = JWT_토큰으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        응답에서_id_정보_있는지_확인(response);
        응답에서_email_정보_확인(response, EMAIL);
        응답에서_나이_정보_확인(response, 20);
    }
}
