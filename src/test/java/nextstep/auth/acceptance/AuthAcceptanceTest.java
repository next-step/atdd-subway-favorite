package nextstep.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.auth.acceptance.model.MemberEntitiesHelper.내_회원_정보_조회_요청;
import static nextstep.auth.acceptance.model.MemberEntitiesHelper.로그인_되어_있음;
import static nextstep.auth.acceptance.model.MemberEntitiesHelper.회원_정보_조회됨;
import static nextstep.auth.acceptance.model.MemberEntitiesHelper.회원가입을_한다;

class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        회원가입을_한다();
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청();
        회원_정보_조회됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        회원가입을_한다();
        String accessToken = 로그인_되어_있음();
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);
        회원_정보_조회됨(response);
    }
}
