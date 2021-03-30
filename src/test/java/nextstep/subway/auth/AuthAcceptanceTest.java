package nextstep.subway.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberSteps.*;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        회원_생성_요청(AuthHelper.EMAIL, AuthHelper.PASSWORD, AuthHelper.AGE);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(AuthHelper.EMAIL, AuthHelper.PASSWORD);

        회원_정보_조회됨(response, AuthHelper.EMAIL, AuthHelper.AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        회원_생성_요청(AuthHelper.EMAIL, AuthHelper.PASSWORD, AuthHelper.AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(AuthHelper.EMAIL, AuthHelper.PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(tokenResponse);

        회원_정보_조회됨(response, AuthHelper.EMAIL, AuthHelper.AGE);
    }
}
